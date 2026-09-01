package com.internship.matcher.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.matcher.model.enums.Enums.AuthProvider;
import com.internship.matcher.model.enums.Enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(length = 255)
    private String passwordHash;

    @Column(length = 15)
    private String phone;

    @Column(length = 120)
    private String qualification; // e.g. B.Tech, B.Sc, MBA

    @Column(length = 120)
    private String fieldOfStudy; // e.g. Computer Science, Commerce

    @Column(length = 150)
    private String collegeOrUniversity;

    private Integer graduationYear;

    @Column(precision = 4)
    private Double cgpaOrPercentage;

    @Column(length = 1000)
    private String careerGoals;

    @Column(length = 1000)
    private String bio;

    @ElementCollection
    @CollectionTable(name = "student_interests", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "interest")
    @Builder.Default
    private Set<String> interests = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "student_preferred_locations", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "location")
    @Builder.Default
    private Set<String> preferredLocations = new HashSet<>();

    @Column(length = 30)
    private String preferredWorkMode; // REMOTE / ONSITE / HYBRID / ANY

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<StudentSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Application> applications = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.STUDENT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;

    private String googleId;

    @Column(length = 500)
    private String profilePictureUrl;

    @Builder.Default
    private boolean emailVerified = false;

    @Builder.Default
    private boolean profileComplete = false;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
