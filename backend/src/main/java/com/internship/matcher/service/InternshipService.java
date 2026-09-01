package com.internship.matcher.service;

import com.internship.matcher.dto.request.InternshipRequest;
import com.internship.matcher.exception.ApiException;
import com.internship.matcher.model.entity.Internship;
import com.internship.matcher.model.entity.InternshipSkill;
import com.internship.matcher.model.entity.Skill;
import com.internship.matcher.model.enums.Enums.WorkMode;
import com.internship.matcher.repository.InternshipRepository;
import com.internship.matcher.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final SkillRepository skillRepository;

    public List<Internship> getAllActive() {
        return internshipRepository.findByActiveTrue();
    }

    public List<Internship> search(String location, String sector, String keyword) {
        return internshipRepository.search(blankToNull(location), blankToNull(sector), blankToNull(keyword));
    }

    public Internship getById(Long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Internship not found"));
    }

    @Transactional
    public Internship create(InternshipRequest request) {
        Internship internship = Internship.builder()
                .title(request.getTitle())
                .companyName(request.getCompanyName())
                .sector(request.getSector())
                .description(request.getDescription())
                .location(request.getLocation())
                .workMode(parseWorkMode(request.getWorkMode()))
                .duration(request.getDuration())
                .stipendPerMonth(request.getStipendPerMonth())
                .minimumQualification(request.getMinimumQualification())
                .capacity(request.getCapacity())
                .applicationDeadline(request.getApplicationDeadline())
                .active(true)
                .build();

        if (request.getInterestTags() != null) {
            internship.getInterestTags().addAll(request.getInterestTags());
        }

        if (request.getRequiredSkills() != null) {
            List<InternshipSkill> skillLinks = new ArrayList<>();
            for (InternshipRequest.SkillRequirement req : request.getRequiredSkills()) {
                if (req.getSkillName() == null || req.getSkillName().isBlank()) continue;
                Skill skill = skillRepository.findByNameIgnoreCase(req.getSkillName().trim())
                        .orElseGet(() -> skillRepository.save(
                                Skill.builder().name(req.getSkillName().trim()).category("General").build()));
                int importance = req.getImportance() == null ? 3 : Math.max(1, Math.min(5, req.getImportance()));
                skillLinks.add(InternshipSkill.builder().internship(internship).skill(skill).importance(importance).build());
            }
            internship.getRequiredSkills().addAll(skillLinks);
        }

        return internshipRepository.save(internship);
    }

    private WorkMode parseWorkMode(String value) {
        if (value == null) return WorkMode.ONSITE;
        try {
            return WorkMode.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return WorkMode.ONSITE;
        }
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
