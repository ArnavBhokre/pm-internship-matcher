package com.internship.matcher.controller;

import com.internship.matcher.dto.request.StudentProfileRequest;
import com.internship.matcher.dto.response.ApiResponse;
import com.internship.matcher.model.entity.Student;
import com.internship.matcher.security.CurrentStudentResolver;
import com.internship.matcher.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final CurrentStudentResolver currentStudentResolver;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyProfile() {
        Student student = studentService.getByEmail(currentStudentResolver.currentEmail());
        return ResponseEntity.ok(ApiResponse.ok("Profile fetched", student));
    }

    /** Create or update the logged-in student's profile: qualifications, skills, interests, locations, goals */
    @PutMapping("/me/profile")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody StudentProfileRequest request) {
        Student updated = studentService.upsertProfile(currentStudentResolver.currentEmail(), request);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", updated));
    }
}
