package com.internship.matcher.service;

import com.internship.matcher.dto.request.StudentProfileRequest;
import com.internship.matcher.exception.ApiException;
import com.internship.matcher.model.entity.Skill;
import com.internship.matcher.model.entity.Student;
import com.internship.matcher.model.entity.StudentSkill;
import com.internship.matcher.model.enums.Enums.ProficiencyLevel;
import com.internship.matcher.repository.SkillRepository;
import com.internship.matcher.repository.StudentRepository;
import com.internship.matcher.repository.StudentSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final EntityManager entityManager;

    public Student getByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    /**
     * Creates or updates a student's full profile: qualifications, interests,
     * preferred locations, career goals, and their skill set with proficiency
     * levels. This is the data the recommendation engine matches against.
     */
    @Transactional
    public Student upsertProfile(String email, StudentProfileRequest request) {
        Student student = getByEmail(email);

        if (request.getFullName() != null) student.setFullName(request.getFullName());
        if (request.getPhone() != null) student.setPhone(request.getPhone());
        student.setQualification(request.getQualification());
        student.setFieldOfStudy(request.getFieldOfStudy());
        student.setCollegeOrUniversity(request.getCollegeOrUniversity());
        student.setGraduationYear(request.getGraduationYear());
        student.setCgpaOrPercentage(request.getCgpaOrPercentage());
        student.setCareerGoals(request.getCareerGoals());
        student.setBio(request.getBio());

        if (request.getInterests() != null) {
            student.getInterests().clear();
            student.getInterests().addAll(request.getInterests());
        }
        if (request.getPreferredLocations() != null) {
            student.getPreferredLocations().clear();
            student.getPreferredLocations().addAll(request.getPreferredLocations());
        }
        student.setPreferredWorkMode(request.getPreferredWorkMode());

     if (request.getSkills() != null) {

    studentSkillRepository.deleteAll(student.getSkills());
    student.getSkills().clear();

    entityManager.flush();
    entityManager.clear();

    student = getByEmail(email);

    List<StudentSkill> newSkills = new ArrayList<>();

    for (StudentProfileRequest.SkillInput input : request.getSkills()) { 
                if (input.getSkillName() == null || input.getSkillName().isBlank()) continue;

                Skill skill = skillRepository.findByNameIgnoreCase(input.getSkillName().trim())
                        .orElseGet(() -> skillRepository.save(
                                Skill.builder().name(input.getSkillName().trim()).category("General").build()));

                ProficiencyLevel level = ProficiencyLevel.INTERMEDIATE;
                if (input.getProficiencyLevel() != null) {
                    try {
                        level = ProficiencyLevel.valueOf(input.getProficiencyLevel().toUpperCase());
                    } catch (IllegalArgumentException ignored) { /* keep default */ }
                }

                newSkills.add(StudentSkill.builder().student(student).skill(skill).proficiencyLevel(level).build());
            }
            student.getSkills().addAll(newSkills);
        }

        student.setProfileComplete(isProfileSufficientlyComplete(student));

        return studentRepository.save(student);
    }

    private boolean isProfileSufficientlyComplete(Student student) {
        return student.getQualification() != null && !student.getQualification().isBlank()
                && !student.getSkills().isEmpty()
                && !student.getInterests().isEmpty()
                && !student.getPreferredLocations().isEmpty();
    }
}
