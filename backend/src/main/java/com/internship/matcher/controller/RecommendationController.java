package com.internship.matcher.controller;

import com.internship.matcher.dto.response.ApiResponse;
import com.internship.matcher.dto.response.RecommendationResponse;
import com.internship.matcher.model.entity.Student;
import com.internship.matcher.security.CurrentStudentResolver;
import com.internship.matcher.service.RecommendationEngine;
import com.internship.matcher.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationEngine recommendationEngine;
    private final StudentService studentService;
    private final CurrentStudentResolver currentStudentResolver;

    /**
     * Returns the logged-in student's personalized, ranked internship
     * recommendations, each with an overall match score plus a skill/
     * interest/location/qualification breakdown and a human-readable
     * explanation of why it was suggested.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getMyRecommendations(@RequestParam(defaultValue = "20") int limit) {
        Student student = studentService.getByEmail(currentStudentResolver.currentEmail());
        List<RecommendationResponse> recommendations = recommendationEngine.generateRecommendations(student, limit);
        return ResponseEntity.ok(ApiResponse.ok("Recommendations generated", recommendations));
    }
}
