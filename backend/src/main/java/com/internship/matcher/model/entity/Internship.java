package com.internship.matcher.model.entity;

import com.internship.matcher.model.enums.Enums.WorkMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "internships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(length = 100)
    private String sector; // e.g. IT, Finance, Manufacturing, Healthcare, Govt

    @Column(length = 3000)
    private String description;

    @Column(length = 120)
    private String location; // city, state

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private WorkMode workMode = WorkMode.ONSITE;

    @Column(length = 60)
    private String duration; // e.g. "6 months"

    private Double stipendPerMonth;

    @Column(length = 200)
    private String minimumQualification; // e.g. "Any Graduate", "B.Tech/B.E."

    @ElementCollection
    @CollectionTable(name = "internship_interest_tags", joinColumns = @JoinColumn(name = "internship_id"))
    @Column(name = "tag")
    @Builder.Default
    private Set<String> interestTags = new HashSet<>();

    @OneToMany(mappedBy = "internship", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<InternshipSkill> requiredSkills = new ArrayList<>();

    private Integer capacity;

    @Builder.Default
    private Integer seatsFilled = 0;

    private LocalDate applicationDeadline;

    @Builder.Default
    private boolean active = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
