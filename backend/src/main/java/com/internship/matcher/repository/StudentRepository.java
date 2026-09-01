package com.internship.matcher.repository;

import com.internship.matcher.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findByGoogleId(String googleId);
    boolean existsByEmail(String email);
}
