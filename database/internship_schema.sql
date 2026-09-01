-- =====================================================================
--  PM Internship Matcher - MySQL Database Schema
--  Database name: internship
--
--  NOTE: You usually do NOT need to run this file manually. Spring Boot
--  (Hibernate ddl-auto=update) will create/update every table automatically
--  the first time you start the backend, as long as the "internship"
--  database itself exists. This script is provided for reference, for
--  manual setup, and so you can inspect the exact structure being used.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS internship CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE internship;

-- ---------------------------------------------------------------------
-- Students (the applicant / job-seeker account)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name              VARCHAR(120) NOT NULL,
    email                  VARCHAR(150) NOT NULL UNIQUE,
    password_hash          VARCHAR(255),
    phone                  VARCHAR(15),
    qualification          VARCHAR(120),
    field_of_study         VARCHAR(120),
    college_or_university  VARCHAR(150),
    graduation_year        INT,
    cgpa_or_percentage     DOUBLE,
    career_goals           VARCHAR(1000),
    bio                    VARCHAR(1000),
    preferred_work_mode    VARCHAR(30),
    role                   VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    auth_provider          VARCHAR(20) NOT NULL DEFAULT 'LOCAL',
    google_id              VARCHAR(120),
    profile_picture_url    VARCHAR(500),
    email_verified         BOOLEAN NOT NULL DEFAULT FALSE,
    profile_complete       BOOLEAN NOT NULL DEFAULT FALSE,
    created_at             DATETIME,
    updated_at             DATETIME
);

CREATE TABLE IF NOT EXISTS student_interests (
    student_id BIGINT NOT NULL,
    interest   VARCHAR(150),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS student_preferred_locations (
    student_id BIGINT NOT NULL,
    location   VARCHAR(150),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- Skills (shared master list) + join tables
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS skills (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS student_skills (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id         BIGINT NOT NULL,
    skill_id           BIGINT NOT NULL,
    proficiency_level  VARCHAR(20) DEFAULT 'INTERMEDIATE',
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- Internships (opportunities posted under the PM Internship Scheme)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS internships (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    title                  VARCHAR(200) NOT NULL,
    company_name           VARCHAR(150) NOT NULL,
    sector                 VARCHAR(100),
    description            VARCHAR(3000),
    location               VARCHAR(120),
    work_mode              VARCHAR(20) DEFAULT 'ONSITE',
    duration               VARCHAR(60),
    stipend_per_month      DOUBLE,
    minimum_qualification  VARCHAR(200),
    capacity               INT,
    seats_filled           INT DEFAULT 0,
    application_deadline   DATE,
    active                 BOOLEAN NOT NULL DEFAULT TRUE,
    created_at             DATETIME
);

CREATE TABLE IF NOT EXISTS internship_interest_tags (
    internship_id BIGINT NOT NULL,
    tag           VARCHAR(150),
    FOREIGN KEY (internship_id) REFERENCES internships(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS internship_skills (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    internship_id  BIGINT NOT NULL,
    skill_id       BIGINT NOT NULL,
    importance     INT DEFAULT 3,
    FOREIGN KEY (internship_id) REFERENCES internships(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- Applications (student <-> internship, with computed match score)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS applications (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id     BIGINT NOT NULL,
    internship_id  BIGINT NOT NULL,
    match_score    DOUBLE,
    status         VARCHAR(20) DEFAULT 'RECOMMENDED',
    created_at     DATETIME,
    updated_at     DATETIME,
    UNIQUE KEY uq_student_internship (student_id, internship_id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (internship_id) REFERENCES internships(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- OTP verification codes (registration / login / password reset)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS otp_verifications (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    email         VARCHAR(150) NOT NULL,
    otp_code      VARCHAR(10) NOT NULL,
    purpose       VARCHAR(20) NOT NULL,
    expires_at    DATETIME NOT NULL,
    verified      BOOLEAN NOT NULL DEFAULT FALSE,
    attempt_count INT DEFAULT 0,
    created_at    DATETIME
);

CREATE INDEX idx_otp_email_purpose ON otp_verifications (email, purpose);
CREATE INDEX idx_internship_location ON internships (location);
CREATE INDEX idx_internship_sector ON internships (sector);
