package com.internship.matcher.repository;

import com.internship.matcher.model.entity.InternshipSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipSkillRepository extends JpaRepository<InternshipSkill, Long> {
    List<InternshipSkill> findByInternshipId(Long internshipId);
}
