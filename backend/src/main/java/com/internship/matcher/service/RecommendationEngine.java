package com.internship.matcher.service;

import com.internship.matcher.dto.response.RecommendationResponse;
import com.internship.matcher.model.entity.*;
import com.internship.matcher.model.enums.Enums.ProficiencyLevel;
import com.internship.matcher.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ================================================================================
 *  AI-BASED MATCHING / RECOMMENDATION ENGINE
 * ================================================================================
 * This engine uses a hybrid approach combining:
 *
 *   1. TF-IDF Cosine Similarity — builds term-frequency / inverse-document-frequency
 *      vectors from student profiles (career goals, bio, interests, field of study)
 *      and internship descriptions (title, description, sector, tags), then uses
 *      cosine similarity to measure semantic alignment.
 *
 *   2. Weighted Multi-Factor Scoring — evaluates skill match, qualification match,
 *      field-of-study relevance, location preference, and work-mode preference.
 *
 *   3. Proficiency-Weighted Skill Overlap — importance-weighted overlap boosted by
 *      the student's proficiency level in each required skill.
 *
 *  Final formula:
 *   overallScore = 0.25 * skillMatch
 *                + 0.25 * tfidfCosineSimilarity
 *                + 0.20 * fieldOfStudyMatch
 *                + 0.12 * qualificationMatch
 *                + 0.10 * locationMatch
 *                + 0.05 * workModeMatch
 *                + 0.03 * careerGoalKeywordMatch
 *
 *  Every recommendation comes with a human-readable explanation.
 * ================================================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationEngine {

    private final InternshipRepository internshipRepository;

    // ── Weight configuration ──
    private static final double W_SKILL         = 0.25;
    private static final double W_TFIDF         = 0.25;
    private static final double W_FIELD         = 0.20;
    private static final double W_QUALIFICATION = 0.12;
    private static final double W_LOCATION      = 0.10;
    private static final double W_WORKMODE      = 0.05;
    private static final double W_CAREER_GOAL   = 0.03;

    // ── Proficiency weights ──
    private static final Map<ProficiencyLevel, Double> PROFICIENCY_WEIGHT = Map.of(
            ProficiencyLevel.BEGINNER, 0.55,
            ProficiencyLevel.INTERMEDIATE, 0.75,
            ProficiencyLevel.ADVANCED, 0.9,
            ProficiencyLevel.EXPERT, 1.0
    );

    // ── Common English stop-words to ignore in TF-IDF ──
    private static final Set<String> STOP_WORDS = Set.of(
        "a", "an", "the", "and", "or", "but", "in", "on", "at", "to", "for",
        "of", "with", "by", "from", "is", "it", "as", "be", "was", "were",
        "are", "been", "has", "have", "had", "do", "does", "did", "will",
        "would", "could", "should", "may", "might", "can", "shall", "not",
        "this", "that", "these", "those", "such", "than", "other", "into",
        "out", "up", "about", "our", "their", "its", "you", "your", "we",
        "they", "he", "she", "him", "her", "his", "them", "who", "which",
        "what", "when", "where", "how", "all", "each", "every", "both",
        "few", "more", "most", "some", "any", "no", "nor", "so", "very",
        "also", "just", "then", "now", "here", "there", "if", "because",
        "while", "during", "before", "after", "above", "below", "between",
        "under", "over", "through", "work", "working", "using", "based"
    );

    // ── Field-of-study → relevant sectors/keywords mapping ──
    private static final Map<String, Set<String>> FIELD_SECTOR_MAP = new HashMap<>();
    static {
        FIELD_SECTOR_MAP.put("computer science", Set.of("it", "software", "technology", "data", "ai", "machine learning", "web", "cloud", "cybersecurity", "devops"));
        FIELD_SECTOR_MAP.put("information technology", Set.of("it", "software", "technology", "data", "web", "cloud", "networking", "cybersecurity"));
        FIELD_SECTOR_MAP.put("electronics", Set.of("it", "electronics", "embedded", "iot", "telecom", "semiconductor", "vlsi", "hardware"));
        FIELD_SECTOR_MAP.put("electrical", Set.of("energy", "power", "electronics", "electrical", "manufacturing", "automation"));
        FIELD_SECTOR_MAP.put("mechanical", Set.of("manufacturing", "automotive", "aerospace", "energy", "mechanical", "cad", "robotics"));
        FIELD_SECTOR_MAP.put("civil", Set.of("construction", "infrastructure", "urban", "real estate", "civil", "architecture", "environment"));
        FIELD_SECTOR_MAP.put("chemical", Set.of("chemical", "pharmaceutical", "manufacturing", "petrochemical", "environment", "biotechnology"));
        FIELD_SECTOR_MAP.put("biotechnology", Set.of("biotechnology", "pharmaceutical", "healthcare", "research", "genetics", "biomedical"));
        FIELD_SECTOR_MAP.put("commerce", Set.of("finance", "accounting", "banking", "insurance", "business", "taxation", "audit"));
        FIELD_SECTOR_MAP.put("business", Set.of("business", "marketing", "management", "consulting", "hr", "operations", "strategy"));
        FIELD_SECTOR_MAP.put("finance", Set.of("finance", "banking", "investment", "insurance", "fintech", "accounting", "audit"));
        FIELD_SECTOR_MAP.put("marketing", Set.of("marketing", "advertising", "media", "digital", "brand", "social media", "pr"));
        FIELD_SECTOR_MAP.put("economics", Set.of("finance", "banking", "government", "research", "policy", "consulting", "data"));
        FIELD_SECTOR_MAP.put("english", Set.of("media", "content", "writing", "journalism", "publishing", "communication", "education"));
        FIELD_SECTOR_MAP.put("journalism", Set.of("media", "journalism", "content", "publishing", "broadcasting", "communication", "pr"));
        FIELD_SECTOR_MAP.put("psychology", Set.of("healthcare", "hr", "counseling", "research", "education", "ngo", "social"));
        FIELD_SECTOR_MAP.put("sociology", Set.of("ngo", "social", "government", "research", "education", "policy", "community"));
        FIELD_SECTOR_MAP.put("political science", Set.of("government", "policy", "ngo", "research", "legal", "public administration", "diplomacy"));
        FIELD_SECTOR_MAP.put("history", Set.of("education", "research", "museum", "archival", "government", "content", "heritage"));
        FIELD_SECTOR_MAP.put("law", Set.of("legal", "government", "compliance", "corporate", "policy", "judiciary", "intellectual property"));
        FIELD_SECTOR_MAP.put("medicine", Set.of("healthcare", "medical", "hospital", "clinical", "pharmaceutical", "research", "public health"));
        FIELD_SECTOR_MAP.put("pharmacy", Set.of("pharmaceutical", "healthcare", "medical", "research", "regulatory", "clinical"));
        FIELD_SECTOR_MAP.put("nursing", Set.of("healthcare", "hospital", "clinical", "public health", "community health"));
        FIELD_SECTOR_MAP.put("agriculture", Set.of("agriculture", "agritech", "food", "farming", "environment", "rural", "government"));
        FIELD_SECTOR_MAP.put("veterinary", Set.of("veterinary", "agriculture", "animal", "research", "government", "dairy"));
        FIELD_SECTOR_MAP.put("education", Set.of("education", "teaching", "edtech", "curriculum", "training", "research"));
        FIELD_SECTOR_MAP.put("design", Set.of("design", "creative", "ui", "ux", "graphic", "media", "advertising", "architecture"));
        FIELD_SECTOR_MAP.put("fashion", Set.of("fashion", "textile", "design", "retail", "merchandising", "apparel"));
        FIELD_SECTOR_MAP.put("architecture", Set.of("architecture", "construction", "urban", "design", "infrastructure", "interior"));
        FIELD_SECTOR_MAP.put("physics", Set.of("research", "energy", "aerospace", "electronics", "semiconductor", "data", "nanotechnology"));
        FIELD_SECTOR_MAP.put("chemistry", Set.of("chemical", "pharmaceutical", "research", "manufacturing", "food", "environment", "material"));
        FIELD_SECTOR_MAP.put("mathematics", Set.of("data", "finance", "research", "it", "analytics", "actuarial", "ai", "machine learning"));
        FIELD_SECTOR_MAP.put("biology", Set.of("biotechnology", "healthcare", "research", "pharmaceutical", "environment", "genetics"));
        FIELD_SECTOR_MAP.put("environmental science", Set.of("environment", "sustainability", "ngo", "government", "research", "climate", "energy"));
        FIELD_SECTOR_MAP.put("hospitality", Set.of("hospitality", "tourism", "hotel", "food", "event", "travel", "customer service"));
        FIELD_SECTOR_MAP.put("data science", Set.of("it", "data", "ai", "machine learning", "analytics", "technology", "research", "fintech"));
        FIELD_SECTOR_MAP.put("statistics", Set.of("data", "analytics", "research", "finance", "government", "actuarial", "it"));
        FIELD_SECTOR_MAP.put("supply chain", Set.of("logistics", "supply chain", "operations", "manufacturing", "retail", "e-commerce"));
        FIELD_SECTOR_MAP.put("human resources", Set.of("hr", "recruitment", "management", "consulting", "training", "organizational"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  PUBLIC API
    // ═══════════════════════════════════════════════════════════════════════

    public List<RecommendationResponse> generateRecommendations(Student student, int limit) {
        List<Internship> candidates = internshipRepository.findByActiveTrue();

        if (candidates.isEmpty()) {
            return List.of();
        }

        // ── Build corpus for TF-IDF (all internships + student profile) ──
        List<String> corpus = new ArrayList<>();
        for (Internship internship : candidates) {
            corpus.add(buildInternshipText(internship));
        }
        String studentText = buildStudentText(student);
        corpus.add(studentText); // last document = student

        // ── Compute IDF from corpus ──
        Map<String, Double> idf = computeIdf(corpus);

        // ── Student TF-IDF vector ──
        Map<String, Double> studentVector = tfidfVector(studentText, idf);

        // ── Score each internship ──
        List<RecommendationResponse> scored = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            Internship internship = candidates.get(i);
            Map<String, Double> internshipVector = tfidfVector(corpus.get(i), idf);
            double tfidfScore = cosineSimilarity(studentVector, internshipVector) * 100.0;

            scored.add(score(student, internship, tfidfScore));
        }

        scored.sort(Comparator.comparingDouble(RecommendationResponse::getOverallMatchScore).reversed());

        if (limit > 0 && scored.size() > limit) {
            return scored.subList(0, limit);
        }
        return scored;
    }

    public RecommendationResponse score(Student student, Internship internship) {
        // Fallback when called without TF-IDF (e.g., from ApplicationService)
        String studentText = buildStudentText(student);
        String internshipText = buildInternshipText(internship);

        // Simple single-document IDF approximation
        List<String> miniCorpus = List.of(studentText, internshipText);
        Map<String, Double> idf = computeIdf(miniCorpus);

        double tfidfScore = cosineSimilarity(
                tfidfVector(studentText, idf),
                tfidfVector(internshipText, idf)
        ) * 100.0;

        return score(student, internship, tfidfScore);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  CORE SCORING
    // ═══════════════════════════════════════════════════════════════════════

    private RecommendationResponse score(Student student, Internship internship, double tfidfScore) {
        SkillMatchResult skillResult  = computeSkillMatch(student, internship);
        double fieldScore             = computeFieldOfStudyMatch(student, internship);
        double qualificationScore     = computeQualificationMatch(student, internship);
        double locationScore          = computeLocationMatch(student, internship);
        double workModeScore          = computeWorkModeMatch(student, internship);
        double careerGoalScore        = computeCareerGoalMatch(student, internship);

        double overall = (W_SKILL * skillResult.score)
                + (W_TFIDF * tfidfScore)
                + (W_FIELD * fieldScore)
                + (W_QUALIFICATION * qualificationScore)
                + (W_LOCATION * locationScore)
                + (W_WORKMODE * workModeScore)
                + (W_CAREER_GOAL * careerGoalScore);

        overall = round1(clamp(overall));

        return RecommendationResponse.builder()
                .internshipId(internship.getId())
                .title(internship.getTitle())
                .companyName(internship.getCompanyName())
                .sector(internship.getSector())
                .description(truncate(internship.getDescription(), 200))
                .location(internship.getLocation())
                .workMode(internship.getWorkMode() != null ? internship.getWorkMode().name() : null)
                .duration(internship.getDuration())
                .stipendPerMonth(internship.getStipendPerMonth())
                .applicationDeadline(internship.getApplicationDeadline() != null
                        ? internship.getApplicationDeadline().format(DateTimeFormatter.ISO_DATE) : null)
                .overallMatchScore(overall)
                .skillMatchScore(round1(skillResult.score))
                .interestMatchScore(round1(tfidfScore))
                .locationMatchScore(round1(locationScore))
                .qualificationMatchScore(round1(qualificationScore))
                .fieldOfStudyMatchScore(round1(fieldScore))
                .matchedSkills(skillResult.matched)
                .missingSkills(skillResult.missing)
                .matchExplanation(buildExplanation(overall, skillResult, tfidfScore, fieldScore,
                        locationScore, qualificationScore, student, internship))
                .build();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TF-IDF + COSINE SIMILARITY
    // ═══════════════════════════════════════════════════════════════════════

    private String buildStudentText(Student student) {
        StringBuilder sb = new StringBuilder();
        append(sb, student.getFieldOfStudy());
        append(sb, student.getQualification());
        append(sb, student.getCareerGoals());
        append(sb, student.getBio());
        if (student.getInterests() != null) {
            student.getInterests().forEach(i -> append(sb, i));
        }
        if (student.getSkills() != null) {
            student.getSkills().forEach(s -> {
                append(sb, s.getSkill().getName());
                if (s.getSkill().getCategory() != null) append(sb, s.getSkill().getCategory());
            });
        }
        return sb.toString();
    }

    private String buildInternshipText(Internship internship) {
        StringBuilder sb = new StringBuilder();
        append(sb, internship.getTitle());
        append(sb, internship.getDescription());
        append(sb, internship.getSector());
        append(sb, internship.getMinimumQualification());
        append(sb, internship.getLocation());
        if (internship.getInterestTags() != null) {
            internship.getInterestTags().forEach(t -> append(sb, t));
        }
        if (internship.getRequiredSkills() != null) {
            internship.getRequiredSkills().forEach(s -> {
                append(sb, s.getSkill().getName());
                if (s.getSkill().getCategory() != null) append(sb, s.getSkill().getCategory());
            });
        }
        return sb.toString();
    }

    /** Tokenize text into meaningful terms (remove stop-words, short tokens). */
    private List<String> tokenize(String text) {
        if (text == null || text.isBlank()) return List.of();
        return Arrays.stream(text.toLowerCase().split("[^a-zA-Z0-9#+]+"))
                .filter(s -> s.length() > 2)
                .filter(s -> !STOP_WORDS.contains(s))
                .collect(Collectors.toList());
    }

    /** Term frequency: count of each term normalized by document length. */
    private Map<String, Double> computeTf(List<String> tokens) {
        Map<String, Long> counts = tokens.stream()
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));
        double total = tokens.size();
        Map<String, Double> tf = new HashMap<>();
        counts.forEach((term, count) -> tf.put(term, count / total));
        return tf;
    }

    /** Inverse document frequency: log(N / (1 + docs containing term)). */
    private Map<String, Double> computeIdf(List<String> corpus) {
        int n = corpus.size();
        Map<String, Integer> docFreq = new HashMap<>();

        for (String doc : corpus) {
            Set<String> uniqueTerms = new HashSet<>(tokenize(doc));
            for (String term : uniqueTerms) {
                docFreq.merge(term, 1, Integer::sum);
            }
        }

        Map<String, Double> idf = new HashMap<>();
        docFreq.forEach((term, df) -> idf.put(term, Math.log((double) n / (1.0 + df)) + 1.0));
        return idf;
    }

    /** TF-IDF vector for a document. */
    private Map<String, Double> tfidfVector(String text, Map<String, Double> idf) {
        List<String> tokens = tokenize(text);
        Map<String, Double> tf = computeTf(tokens);
        Map<String, Double> vector = new HashMap<>();
        tf.forEach((term, tfVal) -> {
            double idfVal = idf.getOrDefault(term, 1.0);
            vector.put(term, tfVal * idfVal);
        });
        return vector;
    }

    /** Cosine similarity between two sparse vectors. */
    private double cosineSimilarity(Map<String, Double> a, Map<String, Double> b) {
        if (a.isEmpty() || b.isEmpty()) return 0.0;

        double dot = 0, normA = 0, normB = 0;
        for (Map.Entry<String, Double> entry : a.entrySet()) {
            double aVal = entry.getValue();
            normA += aVal * aVal;
            Double bVal = b.get(entry.getKey());
            if (bVal != null) {
                dot += aVal * bVal;
            }
        }
        for (double bVal : b.values()) {
            normB += bVal * bVal;
        }

        double denom = Math.sqrt(normA) * Math.sqrt(normB);
        return denom == 0 ? 0 : dot / denom;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  FACTOR: SKILL MATCH (importance-weighted overlap + proficiency)
    // ═══════════════════════════════════════════════════════════════════════

    private SkillMatchResult computeSkillMatch(Student student, Internship internship) {
        List<InternshipSkill> required = internship.getRequiredSkills();
        if (required == null || required.isEmpty()) {
            return new SkillMatchResult(70.0, List.of(), List.of());
        }

        Map<String, ProficiencyLevel> studentSkillMap = student.getSkills().stream()
                .collect(Collectors.toMap(
                        ss -> ss.getSkill().getName().trim().toLowerCase(),
                        StudentSkill::getProficiencyLevel,
                        (a, b) -> a));

        double totalWeight = 0, earnedWeight = 0;
        List<String> matched = new ArrayList<>(), missing = new ArrayList<>();

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

    // ═══════════════════════════════════════════════════════════════════════
    //  FACTOR: FIELD OF STUDY MATCH
    // ═══════════════════════════════════════════════════════════════════════

    private double computeFieldOfStudyMatch(Student student, Internship internship) {
        if (student.getFieldOfStudy() == null || student.getFieldOfStudy().isBlank()) return 50.0;

        String field = student.getFieldOfStudy().trim().toLowerCase();
        String sector = internship.getSector() != null ? internship.getSector().trim().toLowerCase() : "";
        String title = internship.getTitle() != null ? internship.getTitle().trim().toLowerCase() : "";
        String description = internship.getDescription() != null ? internship.getDescription().trim().toLowerCase() : "";
        String combined = sector + " " + title + " " + description;

        // Direct field name match in sector/title/description
        if (combined.contains(field)) return 100.0;

        // Look up known field → sector mapping
        Set<String> relevantSectors = null;
        for (Map.Entry<String, Set<String>> entry : FIELD_SECTOR_MAP.entrySet()) {
            if (field.contains(entry.getKey()) || entry.getKey().contains(field)) {
                relevantSectors = entry.getValue();
                break;
            }
        }

        if (relevantSectors != null) {
            long hits = relevantSectors.stream()
                    .filter(combined::contains)
                    .count();
            if (hits >= 3) return 95.0;
            if (hits >= 2) return 85.0;
            if (hits >= 1) return 70.0;
        }

        // Interest tag overlap with field
        if (internship.getInterestTags() != null) {
            boolean tagMatch = internship.getInterestTags().stream()
                    .anyMatch(tag -> tag.toLowerCase().contains(field) || field.contains(tag.toLowerCase()));
            if (tagMatch) return 75.0;
        }

        return 30.0; // No clear alignment
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  FACTOR: QUALIFICATION MATCH
    // ═══════════════════════════════════════════════════════════════════════

    private double computeQualificationMatch(Student student, Internship internship) {
        String required = internship.getMinimumQualification();
        if (required == null || required.isBlank() || required.equalsIgnoreCase("Any Graduate")) {
            return 90.0;
        }
        if (student.getQualification() == null || student.getQualification().isBlank()) {
            return 40.0;
        }

        String studentQual = student.getQualification().trim().toLowerCase();
        String requiredLower = required.trim().toLowerCase();

        if (requiredLower.contains(studentQual) || studentQual.contains(requiredLower)) {
            return 100.0;
        }

        String[] tokens = requiredLower.split("[/,]");
        for (String token : tokens) {
            String t = token.trim();
            if (!t.isEmpty() && (studentQual.contains(t) || t.contains(studentQual))) {
                return 90.0;
            }
        }
        return 45.0;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  FACTOR: LOCATION MATCH
    // ═══════════════════════════════════════════════════════════════════════

    private double computeLocationMatch(Student student, Internship internship) {
        if (internship.getWorkMode() != null && internship.getWorkMode().name().equals("REMOTE")) {
            return 100.0;
        }

        Set<String> preferred = normalizeSet(student.getPreferredLocations());
        if (preferred.isEmpty() || internship.getLocation() == null) return 50.0;

        String loc = internship.getLocation().trim().toLowerCase();

        if (preferred.contains(loc)) return 100.0;
        if (preferred.stream().anyMatch(p -> loc.contains(p) || p.contains(loc))) return 75.0;
        return 20.0;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  FACTOR: WORK MODE MATCH
    // ═══════════════════════════════════════════════════════════════════════

    private double computeWorkModeMatch(Student student, Internship internship) {
        if (student.getPreferredWorkMode() == null || student.getPreferredWorkMode().isBlank()
                || student.getPreferredWorkMode().equalsIgnoreCase("ANY")) {
            return 80.0;
        }
        if (internship.getWorkMode() == null) return 50.0;

        String preferred = student.getPreferredWorkMode().trim().toUpperCase();
        String actual = internship.getWorkMode().name();

        if (preferred.equals(actual)) return 100.0;
        if (actual.equals("HYBRID")) return 70.0; // hybrid is flexible
        return 30.0;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  FACTOR: CAREER GOAL KEYWORD MATCH
    // ═══════════════════════════════════════════════════════════════════════

    private double computeCareerGoalMatch(Student student, Internship internship) {
        if (student.getCareerGoals() == null || student.getCareerGoals().isBlank()) return 50.0;

        Set<String> goalWords = tokenize(student.getCareerGoals()).stream()
                .collect(Collectors.toSet());
        String haystack = (safe(internship.getTitle()) + " " + safe(internship.getDescription())
                + " " + safe(internship.getSector())).toLowerCase();

        if (goalWords.isEmpty()) return 50.0;

        long hits = goalWords.stream().filter(haystack::contains).count();
        double ratio = hits / (double) goalWords.size();
        return clamp(ratio * 100.0);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  EXPLANATION BUILDER
    // ═══════════════════════════════════════════════════════════════════════

    private String buildExplanation(double overall, SkillMatchResult skillResult,
                                     double tfidf, double fieldMatch,
                                     double location, double qualification,
                                     Student student, Internship internship) {
        StringBuilder sb = new StringBuilder();

        // Overall rating
        if (overall >= 80) sb.append("🌟 Excellent match! ");
        else if (overall >= 65) sb.append("👍 Strong match. ");
        else if (overall >= 50) sb.append("✅ Good match. ");
        else if (overall >= 35) sb.append("⚡ Moderate match. ");
        else sb.append("📋 Limited match. ");

        // Field of study
        if (fieldMatch >= 80 && student.getFieldOfStudy() != null) {
            sb.append("Your ").append(student.getFieldOfStudy()).append(" background is highly relevant. ");
        } else if (fieldMatch < 40 && student.getFieldOfStudy() != null) {
            sb.append("This role is outside your ").append(student.getFieldOfStudy()).append(" specialization. ");
        }

        // Skills
        if (!skillResult.matched.isEmpty()) {
            sb.append("You have ").append(skillResult.matched.size()).append(" of the required skills");
            if (!skillResult.missing.isEmpty()) {
                sb.append(" (consider building: ").append(String.join(", ", skillResult.missing)).append(")");
            }
            sb.append(". ");
        } else if (!skillResult.missing.isEmpty()) {
            sb.append("Skills to develop: ").append(String.join(", ", skillResult.missing)).append(". ");
        }

        // TF-IDF content similarity
        if (tfidf >= 60) sb.append("Your profile content strongly aligns with this role. ");
        else if (tfidf >= 35) sb.append("Some profile overlap with role requirements. ");

        // Location
        if (location >= 90) sb.append("Located in your preferred area. ");
        else if (location < 30) sb.append("Outside your preferred locations. ");

        // Qualification
        if (qualification < 50) sb.append("Your qualification may not fully meet the stated requirement. ");

        return sb.toString().trim();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════════════════

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

    private void append(StringBuilder sb, String text) {
        if (text != null && !text.isBlank()) {
            sb.append(text).append(" ");
        }
    }

    private String safe(String s) { return s == null ? "" : s; }
    private double clamp(double v) { return Math.max(0, Math.min(100, v)); }
    private double round1(double v) { return Math.round(v * 10.0) / 10.0; }

    private String truncate(String text, int maxLen) {
        if (text == null) return null;
        return text.length() <= maxLen ? text : text.substring(0, maxLen) + "...";
    }
}
