package com.internship.matcher.repository;

import com.internship.matcher.model.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {
    List<StudentSkill> findByStudentId(Long studentId);
    void deleteByStudentIdAndSkillId(Long studentId, Long skillId);
    void deleteByStudentId(Long studentId);
}
