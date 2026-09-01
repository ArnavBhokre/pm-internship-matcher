package com.internship.matcher.model.enums;

public class Enums {

    public enum WorkMode {
        REMOTE, ONSITE, HYBRID
    }

    public enum ProficiencyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }

    public enum ApplicationStatus {
        RECOMMENDED, APPLIED, SHORTLISTED, SELECTED, REJECTED, WITHDRAWN
    }

    public enum OtpPurpose {
        REGISTRATION, LOGIN, PASSWORD_RESET
    }

    public enum AuthProvider {
        LOCAL, GOOGLE
    }

    public enum Role {
        STUDENT, ADMIN
    }
}
