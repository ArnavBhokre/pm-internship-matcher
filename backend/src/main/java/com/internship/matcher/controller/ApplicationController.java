
package com.internship.matcher.controller;

import com.internship.matcher.dto.response.ApiResponse;
import com.internship.matcher.model.entity.Application;
import com.internship.matcher.model.entity.Student;
import com.internship.matcher.security.CurrentStudentResolver;
import com.internship.matcher.service.ApplicationService;
import com.internship.matcher.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final StudentService studentService;
    private final CurrentStudentResolver currentStudentResolver;

    // Constructor
    public ApplicationController(
            ApplicationService applicationService,
            StudentService studentService,
            CurrentStudentResolver currentStudentResolver) {

        this.applicationService = applicationService;
        this.studentService = studentService;
        this.currentStudentResolver = currentStudentResolver;
    }

    // Apply for an internship
    @PostMapping("/{internshipId}")
    public ResponseEntity<ApiResponse> apply(
            @PathVariable Long internshipId) {

        Student student =
                studentService.getByEmail(
                        currentStudentResolver.currentEmail()
                );

        Application application =
                applicationService.applyToInternship(
                        student,
                        internshipId
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Applied successfully",
                        application
                )
        );
    }

    // Get current student's applications
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> myApplications() {

        Student student =
                studentService.getByEmail(
                        currentStudentResolver.currentEmail()
                );

        var applications =
                applicationService.getApplicationsForStudent(
                        student.getId()
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Applications fetched",
                        applications
                )
        );
    }

    // Withdraw an application
    @PutMapping("/{applicationId}/withdraw")
    public ResponseEntity<ApiResponse> withdraw(
            @PathVariable Long applicationId) {

        Student student =
                studentService.getByEmail(
                        currentStudentResolver.currentEmail()
                );

        applicationService.withdrawApplication(
                applicationId,
                student.getId()
        );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Application withdrawn successfully",
                        null
                )
        );
    }
}

