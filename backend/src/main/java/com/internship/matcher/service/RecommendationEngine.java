package com.internship.matcher.service;

import com.internship.matcher.dto.response.RecommendationResponse;
import com.internship.matcher.model.entity.*;
import com.internship.matcher.model.enums.Enums.ProficiencyLevel;
import com.internship.matcher.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ================================================================================
 *  AI-BASED MATCHING / RECOMMENDATION ENGINE
 * ================================================================================
 * This is a transparent, explainable, weighted multi-factor scoring model (a
 * content-based recommender). For each candidate internship it computes four
 * sub-scores from the student's profile, blends them into one overall
 * suitability score (0-100), and ranks internships by that score.
 *
 * Because it is explainable and rule-based it needs no training data and gives
 * every recommendation a human-readable "why" — important for a student-facing
 * product. The weighting scheme and individual scoring functions below are the
 * pieces you would swap out later for a learned model (e.g. collaborative
 * filtering or a fine-tuned embedding similarity model) without touching the
 * rest of the application, since everything talks to this class through
 * generateRecommendations().
 *
 *   overallScore = 0.40 * skillMatch
 *                + 0.20 * interestMatch
 *                + 0.20 * locationMatch
 *                + 0.15 * qualificationMatch
 *                + 0.05 * careerGoalKeywordMatch
 * ================================================================================
 */
@Service
@RequiredArgsConstructor
public class RecommendationEngine {

    private final InternshipRepository internshipRepository;

    private static final double W_SKILL = 0.40;
    private static final double W_INTEREST = 0.20;
    private static final double W_LOCATION = 0.20;
    private static final double W_QUALIFICATION = 0.15;
    private static final double W_CAREER_GOAL = 0.05;

    private static final Map<ProficiencyLevel, Double> PROFICIENCY_WEIGHT = Map.of(
            ProficiencyLevel.BEGINNER, 0.55,
            ProficiencyLevel.INTERMEDIATE, 0.75,
            ProficiencyLevel.ADVANCED, 0.9,
            ProficiencyLevel.EXPERT, 1.0
    );

    public List<RecommendationResponse> generateRecommendations(Student student, int limit) {
        List<Internship> candidates = internshipRepository.findByActiveTrue();

        List<RecommendationResponse> scored = new ArrayList<>();
        for (Internship internship : candidates) {
            scored.add(score(student, internship));
        }

        scored.sort(Comparator.comparingDouble(RecommendationResponse::getOverallMatchScore).reversed());

        if (limit > 0 && scored.size() > limit) {
            return scored.subList(0, limit);
        }
        return scored;
    }

    public RecommendationResponse score(Student student, Internship internship) {
        SkillMatchResult skillResult = computeSkillMatch(student, internship);
        double interestScore = computeInterestMatch(student, internship);
        double locationScore = computeLocationMatch(student, internship);
        double qualificationScore = computeQualificationMatch(student, internship);
        double careerGoalScore = computeCareerGoalMatch(student, internship);

        double overall = (W_SKILL * skillResult.score)
                + (W_INTEREST * interestScore)
                + (W_LOCATION * locationScore)
                + (W_QUALIFICATION * qualificationScore)
                + (W_CAREER_GOAL * careerGoalScore);

        overall = round1(clamp(overall));

        return RecommendationResponse.builder()
                .internshipId(internship.getId())
                .title(internship.getTitle())
                .companyName(internship.getCompanyName())
                .sector(internship.getSector())
                .location(internship.getLocation())
                .workMode(internship.getWorkMode() != null ? internship.getWorkMode().name() : null)
                .duration(internship.getDuration())
                .stipendPerMonth(internship.getStipendPerMonth())
                .applicationDeadline(internship.getApplicationDeadline() != null
                        ? internship.getApplicationDeadline().format(DateTimeFormatter.ISO_DATE) : null)
                .overallMatchScore(overall)
                .skillMatchScore(round1(skillResult.score))
                .interestMatchScore(round1(interestScore))
                .locationMatchScore(round1(locationScore))
                .qualificationMatchScore(round1(qualificationScore))
                .matchedSkills(skillResult.matched)
                .missingSkills(skillResult.missing)
                .matchExplanation(buildExplanation(overall, skillResult, interestScore, locationScore, qualificationScore))
                .build();
    }

    // ---------------------------------------------------------------------
    // Skill match: importance-weighted overlap between required skills and
    // the student's skills, boosted by the student's proficiency level.
    // ---------------------------------------------------------------------
    private SkillMatchResult computeSkillMatch(Student student, Internship internship) {
        List<InternshipSkill> required = internship.getRequiredSkills();
        if (required == null || required.isEmpty()) {
            // No specific skill requirements listed -> treat as neutral/open role
            return new SkillMatchResult(70.0, List.of(), List.of());
        }

        Map<String, ProficiencyLevel> studentSkillMap = student.getSkills().stream()
                .collect(Collectors.toMap(
                        ss -> ss.getSkill().getName().trim().toLowerCase(),
                        StudentSkill::getProficiencyLevel,
                        (a, b) -> a));

        double totalWeight = 0;
        double earnedWeight = 0;
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (InternshipSkill reqSkill : required) {
            String skillName = reqSkill.getSkill().getName();
            int importance = reqSkill.getImportance() == null ? 3 : reqSkill.getImportance();
            totalWeight += importance;

            ProficiencyLevel level = studentSkillMap.get(skillName.trim().toLowerCase());
            if (level != null) {
                earnedWeight += importance * PROFICIENCY_WEIGHT.getOrDefault(level, 0.75);
                matched.add(skillName);
            } else {
                missing.add(skillName);
            }
        }

        double score = totalWeight == 0 ? 70.0 : (earnedWeight / totalWeight) * 100.0;
        return new SkillMatchResult(clamp(score), matched, missing);
    }

    // ---------------------------------------------------------------------
    // Interest match: overlap between student interests/career-goal keywords
    // and the internship's sector + interest tags.
    // ---------------------------------------------------------------------
    private double computeInterestMatch(Student student, Internship internship) {
        Set<String> studentInterests = normalizeSet(student.getInterests());
        if (studentInterests.isEmpty()) return 50.0; // neutral if not specified

        Set<String> internshipTags = new HashSet<>(normalizeSet(internship.getInterestTags()));
        if (internship.getSector() != null) internshipTags.add(internship.getSector().trim().toLowerCase());

        if (internshipTags.isEmpty()) return 50.0;

        long overlap = studentInterests.stream().filter(internshipTags::contains).count();
        // partial credit for substring matches (e.g. "web development" vs "web")
        long fuzzyOverlap = studentInterests.stream()
                .filter(si -> internshipTags.stream().anyMatch(tag -> tag.contains(si) || si.contains(tag)))
                .count();

        double bestOverlap = Math.max(overlap, fuzzyOverlap * 0.7);
        double ratio = bestOverlap / (double) studentInterests.size();
        return clamp(ratio * 100.0);
    }

    // ---------------------------------------------------------------------
    // Location match: exact/partial match against preferred locations, with
    // full credit for remote roles regardless of location preference.
    // ---------------------------------------------------------------------
    private double computeLocationMatch(Student student, Internship internship) {
        if (internship.getWorkMode() != null && internship.getWorkMode().name().equals("REMOTE")) {
            return 100.0;
        }

        Set<String> preferred = normalizeSet(student.getPreferredLocations());
        if (preferred.isEmpty() || internship.getLocation() == null) return 50.0;

        String internshipLocation = internship.getLocation().trim().toLowerCase();

        boolean exact = preferred.contains(internshipLocation);
        boolean partial = preferred.stream().anyMatch(p -> internshipLocation.contains(p) || p.contains(internshipLocation));

        if (exact) return 100.0;
        if (partial) return 75.0;
        return 20.0;
    }

    // ---------------------------------------------------------------------
    // Qualification match: does the student meet the minimum qualification?
    // ---------------------------------------------------------------------
    private double computeQualificationMatch(Student student, Internship internship) {
        String required = internship.getMinimumQualification();
        if (required == null || required.isBlank() || required.equalsIgnoreCase("Any Graduate")) {
            return 90.0;
        }
        if (student.getQualification() == null || student.getQualification().isBlank()) {
            return 40.0; // unknown -> can't confirm eligibility
        }

        String studentQual = student.getQualification().trim().toLowerCase();
        String requiredLower = required.trim().toLowerCase();

        if (requiredLower.contains(studentQual) || studentQual.contains(requiredLower)) {
            return 100.0;
        }

        // Split multi-qualification requirements like "B.Tech/B.E./MCA"
        String[] tokens = requiredLower.split("[/,]");
        for (String token : tokens) {
            String t = token.trim();
            if (!t.isEmpty() && (studentQual.contains(t) || t.contains(studentQual))) {
                return 90.0;
            }
        }
        return 45.0;
    }

    // ---------------------------------------------------------------------
    // Career goal match: lightweight keyword overlap between the student's
    // free-text career goals and the internship title/description/sector.
    // ---------------------------------------------------------------------
    private double computeCareerGoalMatch(Student student, Internship internship) {
        if (student.getCareerGoals() == null || student.getCareerGoals().isBlank()) return 50.0;

        Set<String> goalWords = tokenize(student.getCareerGoals());
        String haystack = (safe(internship.getTitle()) + " " + safe(internship.getDescription()) + " " + safe(internship.getSector()))
                .toLowerCase();

        if (goalWords.isEmpty()) return 50.0;

        long hits = goalWords.stream().filter(haystack::contains).count();
        double ratio = hits / (double) goalWords.size();
        return clamp(ratio * 100.0);
    }

    private String buildExplanation(double overall, SkillMatchResult skillResult, double interest, double location, double qualification) {
        StringBuilder sb = new StringBuilder();
        if (overall >= 80) sb.append("Excellent match. ");
        else if (overall >= 60) sb.append("Good match. ");
        else if (overall >= 40) sb.append("Moderate match. ");
        else sb.append("Limited match. ");

        if (!skillResult.matched.isEmpty()) {
            sb.append("You have ").append(skillResult.matched.size()).append(" of the required skills");
            if (!skillResult.missing.isEmpty()) {
                sb.append(" (missing: ").append(String.join(", ", skillResult.missing)).append(")");
            }
            sb.append(". ");
        } else if (!skillResult.missing.isEmpty()) {
            sb.append("You're missing all listed required skills: ").append(String.join(", ", skillResult.missing)).append(". ");
        }

        if (location >= 90) sb.append("Located in your preferred area. ");
        else if (location < 30) sb.append("Outside your preferred locations. ");

        if (interest >= 70) sb.append("Aligns well with your stated interests. ");
        if (qualification < 50) sb.append("Your qualification may not fully meet the stated requirement. ");

        return sb.toString().trim();
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------
    private static class SkillMatchResult {
        final double score;
        final List<String> matched;
        final List<String> missing;

        SkillMatchResult(double score, List<String> matched, List<String> missing) {
            this.score = score;
            this.matched = matched;
            this.missing = missing;
        }
    }

    private Set<String> normalizeSet(Set<String> input) {
        if (input == null) return Set.of();
        return input.stream()
                .filter(Objects::nonNull)
                .map(s -> s.trim().toLowerCase())
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private Set<String> tokenize(String text) {
        return Arrays.stream(text.toLowerCase().split("[^a-zA-Z0-9]+"))
                .filter(s -> s.length() > 3) // skip tiny stopword-ish tokens
                .collect(Collectors.toSet());
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private double clamp(double value) {
        return Math.max(0, Math.min(100, value));
    }

    private double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
