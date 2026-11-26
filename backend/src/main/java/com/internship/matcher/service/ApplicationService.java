package com.internship.matcher.service;

import com.internship.matcher.dto.response.RecommendationResponse;
import com.internship.matcher.exception.ApiException;
import com.internship.matcher.model.entity.Application;
import com.internship.matcher.model.entity.Internship;
import com.internship.matcher.model.entity.Student;
import com.internship.matcher.model.enums.Enums.ApplicationStatus;
import com.internship.matcher.repository.ApplicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final InternshipService internshipService;
    private final RecommendationEngine recommendationEngine;
    private final MailService mailService;

    // Constructor
    public ApplicationService(
            ApplicationRepository applicationRepository,
            InternshipService internshipService,
            RecommendationEngine recommendationEngine,
            MailService mailService) {

        this.applicationRepository = applicationRepository;
        this.internshipService = internshipService;
        this.recommendationEngine = recommendationEngine;
        this.mailService = mailService;
    }

    // Apply for internship
    @Transactional
    public Application applyToInternship(Student student, Long internshipId) {

        applicationRepository
                .findByStudentIdAndInternshipId(
                        student.getId(),
                        internshipId
                )
                .ifPresent(a -> {
                    throw new ApiException(
                            HttpStatus.CONFLICT,
                            "You have already applied to this internship"
                    );
                });

        Internship internship =
                internshipService.getById(internshipId);

        RecommendationResponse scored =
                recommendationEngine.score(
                        student,
                        internship
                );

        Application application =
                Application.builder()
                        .student(student)
                        .internship(internship)
                        .matchScore(scored.getOverallMatchScore())
                        .status(ApplicationStatus.APPLIED)
                        .build();

        Application savedApplication = applicationRepository.save(application);

        // Send confirmation email asynchronously
        mailService.sendApplicationConfirmationEmail(
                student, internship, scored.getOverallMatchScore());

        return savedApplication;
    }

    // Get student's applications
    public List<Application> getApplicationsForStudent(Long studentId) {

        return applicationRepository
                .findByStudentIdOrderByMatchScoreDesc(studentId);
    }

    // Update application status
    @Transactional
    public Application updateStatus(
            Long applicationId,
            ApplicationStatus status) {

        Application application =
                applicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "Application not found"
                                )
                        );

        application.setStatus(status);

        return applicationRepository.save(application);
    }

    // Withdraw application
    @Transactional
    public void withdrawApplication(
            Long applicationId,
            Long studentId) {

        Application application =
                applicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "Application not found"
                                )
                        );

        // Make sure student can withdraw only their own application
        if (application.getStudent() == null
                || !application.getStudent()
                        .getId()
                        .equals(studentId)) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "You can only withdraw your own application"
            );
        }

        application.setStatus(
                ApplicationStatus.WITHDRAWN
        );

        applicationRepository.save(application);
    }
}
