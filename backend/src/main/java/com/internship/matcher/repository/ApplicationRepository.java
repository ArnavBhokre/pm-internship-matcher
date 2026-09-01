package com.internship.matcher.repository;

import com.internship.matcher.model.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentIdOrderByMatchScoreDesc(Long studentId);
    Optional<Application> findByStudentIdAndInternshipId(Long studentId, Long internshipId);
    List<Application> findByInternshipId(Long internshipId);
}
