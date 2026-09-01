package com.internship.matcher.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class InternshipRequest {
    private String title;
    private String companyName;
    private String sector;
    private String description;
    private String location;
    private String workMode; // REMOTE / ONSITE / HYBRID
    private String duration;
    private Double stipendPerMonth;
    private String minimumQualification;
    private Set<String> interestTags;
    private List<SkillRequirement> requiredSkills;
    private Integer capacity;
    private LocalDate applicationDeadline;

    @Data
    public static class SkillRequirement {
        private String skillName;
        private Integer importance; // 1-5
    }
}
