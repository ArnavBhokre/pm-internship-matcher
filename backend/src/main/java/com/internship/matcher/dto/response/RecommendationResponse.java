package com.internship.matcher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResponse {
    private Long internshipId;
    private String title;
    private String companyName;
    private String sector;
    private String location;
    private String workMode;
    private String duration;
    private Double stipendPerMonth;
    private String applicationDeadline;
    private double overallMatchScore;   // 0-100
    private double skillMatchScore;     // 0-100
    private double interestMatchScore;  // 0-100
    private double locationMatchScore;  // 0-100
    private double qualificationMatchScore; // 0-100
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String matchExplanation;
}
