package com.internship.matcher.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class StudentProfileRequest {
    private String fullName;
    private String phone;
    private String qualification;
    private String fieldOfStudy;
    private String collegeOrUniversity;
    private Integer graduationYear;
    private Double cgpaOrPercentage;
    private String careerGoals;
    private String bio;
    private Set<String> interests;
    private Set<String> preferredLocations;
    private String preferredWorkMode;
    private List<SkillInput> skills;

    @Data
    public static class SkillInput {
        private String skillName;
        private String proficiencyLevel; // BEGINNER / INTERMEDIATE / ADVANCED / EXPERT
    }
}
