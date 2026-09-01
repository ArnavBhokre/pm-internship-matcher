package com.internship.matcher.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "internship_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternshipSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "internship_id", nullable = false)
    @JsonIgnoreProperties({"requiredSkills"})
    private Internship internship;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    /** How critical this skill is to the role, 1 (nice-to-have) to 5 (mandatory) */
    @Builder.Default
    private Integer importance = 3;
}
