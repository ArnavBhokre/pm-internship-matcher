# PM Internship Matcher 🚀

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%20%7C%2021-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![AI Engine](https://img.shields.io/badge/AI%20Engine-TF--IDF%20%2B%20Cosine%20Similarity-purple.svg)]()
[![Security](https://img.shields.io/badge/Security-JWT%20%2B%20BCrypt-red.svg)]()

> **An AI-powered internship recommendation and application platform** that intelligently matches students with opportunities across all major educational disciplines using Natural Language Processing (NLP) and multi-factor scoring, with automated confirmation email notifications.

---

## 📌 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [System Architecture](#-system-architecture)
- [AI Recommendation Engine](#-ai-recommendation-engine)
- [Internship Database Coverage](#-internship-database-coverage)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Database Setup](#1-database-setup)
  - [Backend Setup & Run](#2-backend-setup--run)
  - [Frontend Setup & Run](#3-frontend-setup--run)
- [API Endpoints](#-api-endpoints)
- [Documentation & Resources](#-documentation--resources)
- [License](#-license)

---

## 🌟 Overview

The **PM Internship Matcher** connects students with curated internship listings matching their academic profile, technical proficiencies, career aspirations, and geographical preferences. Unlike basic keyword search systems, this platform employs a custom **TF-IDF (Term Frequency - Inverse Document Frequency) cosine similarity algorithm** and **multi-factor weighted scoring** to calculate semantic relevance and recommend the most suitable roles.

---

## ⚡ Key Features

- **🤖 Hybrid AI Recommendation Engine**: Combines NLP-driven TF-IDF cosine similarity, academic discipline mapping, and importance-weighted skill scoring.
- **📚 Comprehensive Educational Coverage**: 100+ seeded internships spanning **19+ disciplines** (Computer Science, Mechanical, Civil, Commerce, Finance, Healthcare, Pharmacy, Design, Arts, Agriculture, Law, etc.).
- **📧 Automated Email Notifications**: Instant professional HTML confirmation emails upon internship application, plus OTP verification for password recovery via asynchronous Gmail SMTP.
- **👤 Rich Student Profile**: Captures education qualifications, fields of study, technical skills with 4 proficiency levels (Beginner to Expert), interest tags, preferred locations, and career goals.
- **🔐 Secure Authentication**: Stateless JWT-based authentication, BCrypt password hashing, and role-based access control.
- **📊 Real-time Dashboard**: Track submitted applications, application statuses (`APPLIED`, `SHORTLISTED`, `SELECTED`), match scores, and profile completeness.
- **🔍 Search & Filter**: Filter internships by sector, work mode (`REMOTE`, `HYBRID`, `ONSITE`), location, and stipend.

---

## 🏗️ System Architecture

```text
┌────────────────────────────────────────────────────────┐
│                  FRONTEND (Port 5500)                  │
│       HTML5 / CSS3 / Vanilla JavaScript (ES6+)         │
│  - Landing & Auth (index.html, register.html)          │
│  - Student Dashboard & Profile (dashboard, profile)    │
│  - AI Recommendations (recommendations.html)           │
│  - Internship Browser (browse.html, applications.html) │
└───────────────────────────┬────────────────────────────┘
                            │ RESTful JSON APIs
                            ▼
┌────────────────────────────────────────────────────────┐
│                   BACKEND (Port 8080)                  │
│                    Spring Boot 3.3.2                   │
│                                                        │
│  ┌─────────────────────────┐ ┌──────────────────────┐  │
│  │     REST Controllers    │ │ Security (JWT + CORS)│  │
│  └────────────┬────────────┘ └──────────────────────┘  │
│               ▼                                        │
│  ┌─────────────────────────┐ ┌──────────────────────┐  │
│  │     Business Services   │ │  AI Engine (TF-IDF)  │  │
│  └────────────┬────────────┘ └──────────────────────┘  │
│               ▼                                        │
│  ┌─────────────────────────┐ ┌──────────────────────┐  │
│  │    Spring Data JPA      │ │ Async Mail Service   │  │
│  └────────────┬────────────┘ └──────────────────────┘  │
└───────────────┼────────────────────────────────────────┘
                ▼
┌────────────────────────────────────────────────────────┐
│                  MySQL 8.0 (Port 3306)                 │
│                   Database: internship                 │
│  - students, internships, applications                 │
│  - skills, student_skills, internship_skills           │
│  - student_interests, internship_interest_tags         │
└────────────────────────────────────────────────────────┘
```

---

## 🧠 AI Recommendation Engine

The recommendation engine (`RecommendationEngine.java`) evaluates each internship against student profiles using a **7-factor weighted scoring model**:

| Factor | Weight | Evaluation Method |
| :--- | :---: | :--- |
| **Skill Match** | **25%** | Importance-weighted overlap considering student proficiency (Beginner to Expert) |
| **TF-IDF Content Similarity** | **25%** | Cosine similarity of vectorized student bio, skills, and career goals vs. role description |
| **Field of Study** | **20%** | Cross-domain mapping connecting 35+ degrees to industry sectors |
| **Qualification Match** | **12%** | Student degree vs. minimum qualification requirement |
| **Location Preference** | **10%** | Match score with preferred work locations |
| **Work Mode** | **5%** | Remote, Hybrid, or On-site preference alignment |
| **Career Goals** | **3%** | Role and sector keyword overlap with student career goals |

### Transparent Explanations
Each recommendation is accompanied by an AI-generated explanation detailing **why** the role was recommended (e.g., *"Strong skill match (85%) • Your Computer Science background aligns well with the IT sector • High content similarity (72%)"*).

---

## 📊 Internship Database Coverage

The database is pre-seeded with 100+ internship openings covering diverse academic fields:

- **Computer Science & IT**: AI/ML, Cloud DevOps, Cybersecurity, Full-Stack, Mobile Dev, Data Science
- **Core Engineering**: Electronics, Mechanical, Civil, Chemical, Robotics, IoT, VLSI
- **Pure Sciences**: Physics R&D, Chemistry, Molecular Biology, Environmental Science, Mathematics
- **Commerce & Finance**: FinTech, Investment Banking, Accounting, Taxation, Financial Analysis
- **Business & Marketing**: Digital Marketing, Brand Management, Market Research, SEO/SEM
- **Healthcare & Pharma**: Clinical Research, Pharmacovigilance, Public Health, Bioinformatics
- **Design & Creative**: UI/UX Design, Graphic Design, Fashion, Interior Design
- **Law & Public Policy**: Corporate Law, Intellectual Property, Legal Research
- **Agriculture & Sustainability**: AgriTech, Food Technology, Sustainable Development
- **Arts, Media & Education**: Journalism, Content Creation, EdTech, Psychology Research

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.3.2
- **Language**: Java 17 / 21
- **Database & ORM**: MySQL 8.0, Hibernate / Spring Data JPA
- **Security**: Spring Security 6, JJWT (JSON Web Token), BCrypt
- **Email Service**: Spring Mail, Jakarta Mail (Gmail SMTP via TLS)
- **Build Tool**: Apache Maven 3.9.16

### Frontend
- **Languages**: HTML5, CSS3, Vanilla JavaScript (ES6+)
- **Architecture**: Modular client, responsive CSS variables, Fetch API
- **Web Server**: Python HTTP Server / Live Server (Port 5500)

---

## 📂 Project Structure

```text
pm-internship-matcher/
├── backend/
│   ├── src/main/java/com/internship/matcher/
│   │   ├── MatcherApplication.java           # Spring Boot entry point (@EnableAsync)
│   │   ├── config/                           # Security, CORS, OAuth2 configuration
│   │   ├── controller/                       # REST endpoints (Auth, Students, Internships, Applications, Recommendations)
│   │   ├── dto/                              # Request and Response transfer objects
│   │   ├── model/entity/                     # JPA entity definitions (Student, Internship, Application, etc.)
│   │   ├── repository/                       # Spring Data JPA repositories
│   │   ├── security/                         # JWT filters, UserDetailsService
│   │   ├── service/
│   │   │   ├── RecommendationEngine.java     # ★ TF-IDF AI & Cosine Similarity Engine
│   │   │   ├── ApplicationService.java       # Application submission & async email trigger
│   │   │   ├── MailService.java              # HTML Email templates & SMTP dispatcher
│   │   │   ├── AuthService.java              # Registration, Login & OTP handling
│   │   │   └── InternshipService.java        # Internship catalog operations
│   │   └── util/                             # JWT tokens utility
│   ├── src/main/resources/
│   │   └── application.properties            # Datasource, JWT, and SMTP settings
│   ├── pom.xml                               # Maven project dependencies
│   └── Dockerfile                            # Containerization setup
├── frontend/
│   ├── index.html                            # Login interface
│   ├── register.html                         # Student registration
│   ├── dashboard.html                        # Application tracker & dashboard
│   ├── profile.html                          # Profile management (skills, field of study)
│   ├── recommendations.html                  # AI-ranked recommendation cards
│   ├── browse.html                           # Full internship search & directory
│   ├── forgot-password.html                  # Password reset with OTP
│   ├── css/style.css                         # Global responsive stylesheet
│   └── js/                                   # Client-side API and page logic
├── database/
│   ├── internship_schema.sql                 # Database table DDL
│   └── seed_data.sql                         # 100+ internships, 92 skills seed data
├── PM-Internship-Matcher-Project-Guide.pdf   # Complete project documentation PDF
└── README.md                                 # Project documentation
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher installed and added to `PATH`
- **MySQL Server 8.0** running locally on port `3306`
- **Python 3.x** (for running the lightweight frontend server)

---

### 1. Database Setup

1. Start your local MySQL service.
2. Create the database:
   ```sql
   CREATE DATABASE IF NOT EXISTS internship;
   ```
3. Seed the database with 100+ internships and skills:
   ```powershell
   Get-Content "database\seed_data.sql" -Raw | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p internship
   ```

---

### 2. Backend Setup & Run

1. Navigate to the backend folder:
   ```powershell
   cd backend
   ```
2. Configure your database credentials and Gmail App Password in `src/main/resources/application.properties` (or pass via environment variables):
   ```properties
   spring.datasource.username=root
   spring.datasource.password=root

   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-gmail-app-password
   ```
3. Run the backend using Maven:
   ```powershell
   # Windows (using bundled maven)
   ..\apache-maven-3.9.16\bin\mvn.cmd spring-boot:run

   # Or standard maven
   mvn spring-boot:run
   ```
4. Verify the backend is active at: `http://localhost:8080`

---

### 3. Frontend Setup & Run

1. Open a new terminal and navigate to the frontend folder:
   ```powershell
   cd frontend
   ```
2. Start the local HTTP server:
   ```powershell
   python -m http.server 5500
   ```
3. Open your browser and navigate to:
   👉 **`http://localhost:5500/index.html`**

---

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Create student account
- `POST /api/auth/login` - Authenticate and obtain JWT
- `POST /api/auth/forgot-password` - Request 6-digit OTP via email
- `POST /api/auth/reset-password` - Reset password using OTP

### Student Profile
- `GET /api/students/profile` - Retrieve current profile
- `PUT /api/students/profile` - Update profile, qualifications, skills, and locations

### Internships & Recommendations
- `GET /api/internships` - Browse internship catalog with search and filters
- `GET /api/internships/{id}` - Retrieve detailed internship specification
- `GET /api/recommendations` - Retrieve personalized AI-recommended internships

### Applications
- `POST /api/applications/{internshipId}` - Apply to an internship (triggers async confirmation email)
- `GET /api/applications` - View all submitted applications

---

## 📄 Documentation & Resources

- 📑 **Complete Project Guide (PDF)**: [`PM-Internship-Matcher-Project-Guide.pdf`](./PM-Internship-Matcher-Project-Guide.pdf)
- 📊 **Project Presentation**: Available in the repository root.

---

## 📜 License

This project is developed for educational and portfolio purposes. Feel free to use and adapt it!
