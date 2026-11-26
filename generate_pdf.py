from fpdf import FPDF

class ProjectGuidePDF(FPDF):
    def header(self):
        if self.page_no() > 1:
            self.set_font('Helvetica', 'I', 8)
            self.set_text_color(150, 150, 150)
            self.cell(0, 8, 'PM Internship Matcher - Project Guide', align='C')
            self.ln(4)
            self.set_draw_color(67, 97, 238)
            self.set_line_width(0.3)
            self.line(10, self.get_y(), 200, self.get_y())
            self.ln(6)

    def footer(self):
        self.set_y(-15)
        self.set_font('Helvetica', 'I', 8)
        self.set_text_color(150, 150, 150)
        self.cell(0, 10, f'Page {self.page_no()}/{{nb}}', align='C')

    def section_title(self, title):
        self.set_font('Helvetica', 'B', 15)
        self.set_text_color(67, 97, 238)
        self.cell(0, 10, title, new_x="LMARGIN", new_y="NEXT")
        self.set_draw_color(67, 97, 238)
        self.set_line_width(0.5)
        self.line(10, self.get_y(), 200, self.get_y())
        self.ln(4)

    def sub_title(self, title):
        self.set_font('Helvetica', 'B', 12)
        self.set_text_color(45, 55, 72)
        self.cell(0, 8, title, new_x="LMARGIN", new_y="NEXT")
        self.ln(2)

    def body_text(self, text):
        self.set_font('Helvetica', '', 10)
        self.set_text_color(30, 30, 30)
        self.multi_cell(0, 5.5, text)
        self.ln(2)

    def bold_text(self, text):
        self.set_font('Helvetica', 'B', 10)
        self.set_text_color(30, 30, 30)
        self.multi_cell(0, 5.5, text)
        self.ln(1)

    def bullet(self, text):
        self.set_font('Helvetica', '', 10)
        self.set_text_color(30, 30, 30)
        x = self.get_x()
        self.cell(8, 5.5, '-')
        self.multi_cell(0, 5.5, text)
        self.ln(1)

    def code_block(self, text):
        self.set_font('Courier', '', 9)
        self.set_fill_color(30, 41, 59)
        self.set_text_color(226, 232, 240)
        y = self.get_y()
        lines = text.split('\n')
        h = len(lines) * 5 + 8
        self.rect(10, y, 190, h, 'F')
        self.set_xy(14, y + 4)
        for line in lines:
            self.cell(0, 5, line, new_x="LMARGIN", new_y="NEXT")
            self.set_x(14)
        self.set_text_color(30, 30, 30)
        self.ln(4)

    def info_box(self, text, color='blue'):
        colors = {
            'blue': (67, 97, 238, 238, 242, 255),
            'green': (16, 185, 129, 236, 253, 245),
            'orange': (245, 158, 11, 255, 251, 235),
        }
        bc, gc = colors.get(color, colors['blue'])[:3], colors.get(color, colors['blue'])[3:]
        y = self.get_y()
        self.set_fill_color(*gc)
        lines = text.split('\n')
        h = len(lines) * 5.5 + 8
        self.rect(10, y, 190, h, 'F')
        self.set_draw_color(*bc)
        self.set_line_width(1)
        self.line(10, y, 10, y + h)
        self.set_xy(14, y + 4)
        self.set_font('Helvetica', '', 9)
        self.set_text_color(30, 30, 30)
        for line in lines:
            self.cell(0, 5.5, line, new_x="LMARGIN", new_y="NEXT")
            self.set_x(14)
        self.ln(4)

    def add_table(self, headers, rows, col_widths=None):
        if col_widths is None:
            col_widths = [190 / len(headers)] * len(headers)
        # Header
        self.set_font('Helvetica', 'B', 9)
        self.set_fill_color(67, 97, 238)
        self.set_text_color(255, 255, 255)
        for i, h in enumerate(headers):
            self.cell(col_widths[i], 7, h, border=1, fill=True, align='C')
        self.ln()
        # Rows
        self.set_font('Helvetica', '', 9)
        self.set_text_color(30, 30, 30)
        for ri, row in enumerate(rows):
            fill = ri % 2 == 1
            if fill:
                self.set_fill_color(248, 250, 252)
            else:
                self.set_fill_color(255, 255, 255)
            max_h = 7
            # Calculate max height for this row
            cell_lines = []
            for i, cell in enumerate(row):
                lines = self.multi_cell(col_widths[i], 5, str(cell), split_only=True)
                cell_lines.append(lines)
                h = len(lines) * 5
                if h > max_h:
                    max_h = h
            y_start = self.get_y()
            x_start = self.get_x()
            for i, cell in enumerate(row):
                x = x_start + sum(col_widths[:i])
                self.set_xy(x, y_start)
                self.rect(x, y_start, col_widths[i], max_h, 'DF' if fill else 'D')
                self.set_xy(x + 1, y_start + 1)
                self.multi_cell(col_widths[i] - 2, 5, str(cell))
            self.set_xy(x_start, y_start + max_h)
        self.ln(4)


pdf = ProjectGuidePDF()
pdf.alias_nb_pages()
pdf.set_auto_page_break(auto=True, margin=20)

# =================== COVER PAGE ===================
pdf.add_page()
pdf.ln(40)
pdf.set_font('Helvetica', 'B', 32)
pdf.set_text_color(67, 97, 238)
pdf.cell(0, 15, 'PM Internship Matcher', align='C', new_x="LMARGIN", new_y="NEXT")
pdf.ln(4)
pdf.set_font('Helvetica', '', 14)
pdf.set_text_color(100, 100, 100)
pdf.cell(0, 10, 'AI-Powered Internship Recommendation & Application Platform', align='C', new_x="LMARGIN", new_y="NEXT")
pdf.ln(10)

# Tech badges
pdf.set_font('Helvetica', 'B', 10)
badges = ['Spring Boot 3.3', 'Java 17', 'MySQL 8.0', 'HTML/CSS/JS', 'TF-IDF AI', 'REST API']
total_w = len(badges) * 28 + (len(badges)-1) * 3
start_x = (210 - total_w) / 2
pdf.set_x(start_x)
for b in badges:
    pdf.set_fill_color(224, 231, 255)
    pdf.set_text_color(67, 97, 238)
    pdf.cell(28, 8, b, fill=True, align='C')
    pdf.cell(3)
pdf.ln(15)

# Status badge
pdf.set_fill_color(67, 97, 238)
pdf.set_text_color(255, 255, 255)
pdf.set_font('Helvetica', 'B', 11)
w = 60
pdf.set_x((210 - w) / 2)
pdf.cell(w, 10, 'Complete & Functional', fill=True, align='C')
pdf.ln(20)

# Meta info
pdf.set_font('Helvetica', '', 11)
pdf.set_text_color(100, 100, 100)
pdf.cell(0, 7, 'Developer: Arnav Bhokre', align='C', new_x="LMARGIN", new_y="NEXT")
pdf.cell(0, 7, 'Date: November 2026', align='C', new_x="LMARGIN", new_y="NEXT")

# =================== TABLE OF CONTENTS ===================
pdf.add_page()
pdf.section_title('Table of Contents')
toc = [
    '1.  Project Overview',
    '2.  System Architecture',
    '3.  AI Recommendation Engine',
    '4.  Features Overview',
    '5.  Internship Database Coverage',
    '6.  Email Notification System',
    '7.  Tech Stack & Dependencies',
    '8.  Project Structure',
    '9.  How to Run the Project',
    '10. API Endpoints',
    '11. Key Files Reference',
    '12. Database Schema',
]
for item in toc:
    pdf.set_font('Helvetica', '', 11)
    pdf.set_text_color(30, 30, 30)
    pdf.cell(0, 7, item, new_x="LMARGIN", new_y="NEXT")

# =================== 1. PROJECT OVERVIEW ===================
pdf.add_page()
pdf.section_title('1. Project Overview')
pdf.body_text(
    'PM Internship Matcher is a full-stack web application that uses Artificial Intelligence '
    'to match students with the most relevant internship opportunities based on their education, '
    'skills, interests, and career goals. The platform covers 100+ internships across all major '
    'education fields in India and sends automated confirmation emails when students apply.'
)
pdf.sub_title('Key Objectives')
pdf.bullet('Provide AI-driven personalized internship recommendations using NLP (TF-IDF) and multi-factor scoring')
pdf.bullet('Cover internships across every major educational discipline - from Engineering to Veterinary Sciences')
pdf.bullet('Automate the application process with professional email confirmations')
pdf.bullet('Offer a clean, responsive web interface for browsing, filtering, and applying to internships')

# =================== 2. SYSTEM ARCHITECTURE ===================
pdf.ln(6)
pdf.section_title('2. System Architecture')
pdf.body_text('The application follows a 3-tier architecture with a REST API connecting the frontend and backend:')
pdf.ln(2)

arch_text = """  FRONTEND (Port 5500)              BACKEND (Port 8080)
  HTML / CSS / JavaScript    <-->   Spring Boot 3.3.2
  - Login / Register                - REST Controllers
  - Dashboard                       - Security (JWT + CORS)
  - Profile Management              - AI Engine (TF-IDF)
  - Browse Internships              - Services Layer
  - AI Recommendations              - JPA / Hibernate ORM
  - Apply to Internships            - Mail Service (Gmail SMTP)
                                           |
                                    MySQL 8.0 (Port 3306)
                                    Database: internship"""
pdf.code_block(arch_text)

# =================== 3. AI RECOMMENDATION ENGINE ===================
pdf.add_page()
pdf.section_title('3. AI Recommendation Engine')
pdf.body_text(
    'The recommendation engine uses a hybrid AI approach combining Natural Language Processing (NLP) '
    'with domain-specific heuristics. It is implemented in RecommendationEngine.java.'
)

pdf.sub_title('3.1 TF-IDF Cosine Similarity (NLP)')
pdf.body_text(
    'The engine builds Term Frequency-Inverse Document Frequency (TF-IDF) vectors from both '
    'the student\'s profile text (bio, skills, interests, career goals) and each internship\'s description. '
    'It then computes the cosine similarity between these vectors to measure semantic alignment.'
)
pdf.info_box(
    'How it works:\n'
    '1. Tokenize and normalize student profile and internship descriptions\n'
    '2. Build a vocabulary from the combined corpus\n'
    '3. Compute TF (term frequency) for each document\n'
    '4. Compute IDF (inverse document frequency) across all documents\n'
    '5. Multiply TF x IDF to get weighted vectors\n'
    '6. Calculate cosine similarity = (A . B) / (|A| x |B|)'
)

pdf.sub_title('3.2 Field-of-Study Matching')
pdf.body_text(
    'Maps 35+ academic fields to relevant industry sectors. For example, a student studying '
    '"Computer Science" is matched to sectors like IT, Software, Data Science, and AI. A student '
    'studying "Pharmacy" is matched to Pharmaceutical, Healthcare, and Biotech sectors.'
)

pdf.sub_title('3.3 Multi-Factor Weighted Scoring')
pdf.body_text('The final recommendation score is a weighted combination of 7 factors:')
pdf.add_table(
    ['Factor', 'Weight', 'Description'],
    [
        ['Skill Match', '25%', 'Importance-weighted skill overlap + proficiency'],
        ['TF-IDF Content Similarity', '25%', 'NLP-based semantic text similarity'],
        ['Field of Study', '20%', 'Academic field to sector relevance'],
        ['Qualification Match', '12%', 'Degree level vs requirement'],
        ['Location Preference', '10%', 'Preferred location alignment'],
        ['Work Mode', '5%', 'Remote/Onsite/Hybrid preference'],
        ['Career Goals', '3%', 'Keyword overlap with role'],
    ],
    [45, 20, 125]
)

pdf.sub_title('3.4 Human-Readable Explanations')
pdf.body_text(
    'Every recommendation includes a generated explanation string (e.g., "Strong skill match (85%) - '
    'Your Computer Science background aligns well with the IT sector - High content similarity (72%)"), '
    'making the AI reasoning transparent to the user.'
)

# =================== 4. FEATURES OVERVIEW ===================
pdf.add_page()
pdf.section_title('4. Features Overview')

features = [
    ('Authentication & Security', 'JWT-based authentication with login, registration, and forgot password (OTP via email). Passwords hashed with BCrypt.'),
    ('Student Profile Management', 'Complete profile with field of study, qualification, skills with proficiency, interests, preferred locations, work mode, and career goals.'),
    ('AI Recommendations', 'TF-IDF + cosine similarity engine with 7-factor scoring. Shows match percentage, field-of-study alignment, and content similarity bars.'),
    ('Browse & Filter', 'Browse all 100+ internships with filtering by sector, location, work mode, and search. Paginated results with detailed cards.'),
    ('One-Click Apply', 'Apply to internships directly from recommendation or browse pages. Duplicate application prevention built-in.'),
    ('Email Notifications', 'Professional HTML confirmation email sent asynchronously on application. OTP emails for password reset.'),
    ('Dashboard', 'Overview of applied internships, application status tracking, and profile completion indicator.'),
    ('Responsive UI', 'Clean, modern vanilla HTML/CSS/JS frontend with mobile-responsive design. No heavy frameworks needed.'),
]
for title, desc in features:
    pdf.set_font('Helvetica', 'B', 10)
    pdf.set_text_color(67, 97, 238)
    pdf.cell(0, 6, title, new_x="LMARGIN", new_y="NEXT")
    pdf.set_font('Helvetica', '', 10)
    pdf.set_text_color(80, 80, 80)
    pdf.multi_cell(0, 5.5, desc)
    pdf.ln(3)

# =================== 5. INTERNSHIP DATABASE ===================
pdf.add_page()
pdf.section_title('5. Internship Database Coverage')
pdf.body_text('The platform seeds 100+ internships covering all major education fields in India:')

pdf.add_table(
    ['Category', 'Fields Covered', 'Count'],
    [
        ['Computer Science & IT', 'Software, Web, Mobile, Cloud, DevOps, Cyber, AI/ML, Data', '15+'],
        ['Electronics & Electrical', 'Embedded, IoT, VLSI, Power Systems, Robotics', '6+'],
        ['Mechanical & Civil', 'CAD/CAM, Structural, Construction, Automotive', '6+'],
        ['Chemical Engineering', 'Process Engineering, Petrochemical, Materials', '4+'],
        ['Sciences', 'Physics, Chemistry, Biology, Environment, Math', '8+'],
        ['Commerce & Finance', 'Banking, Financial Analysis, Accounting, FinTech', '8+'],
        ['Marketing & Sales', 'Digital Marketing, Brand, SEO/SEM, Research', '6+'],
        ['Arts & Humanities', 'Content Writing, Psychology, Sociology, History', '6+'],
        ['Law & Legal', 'Legal Research, Corporate Law, IP, Public Policy', '5+'],
        ['Healthcare & Medicine', 'Clinical Research, Public Health, Hospital Admin', '5+'],
        ['Pharmacy & Biotech', 'Drug Research, Pharma, Regulatory, Bioinformatics', '5+'],
        ['Agriculture', 'AgriTech, Food Science, Dairy Technology', '4+'],
        ['Design', 'UI/UX, Graphic, Fashion, Interior Design', '5+'],
        ['Architecture', 'Sustainable, Urban Planning, Landscape', '3+'],
        ['Education', 'EdTech, Teaching, Curriculum, E-Learning', '4+'],
        ['Media & Journalism', 'Content, Video Production, PR, Broadcasting', '4+'],
        ['Hospitality', 'Hotel Management, Events, Tourism', '3+'],
        ['Veterinary Sciences', 'Animal Health, Dairy Science, Wildlife', '3+'],
        ['Supply Chain', 'Logistics, Operations, Procurement', '3+'],
    ],
    [50, 115, 25]
)

# =================== 6. EMAIL SYSTEM ===================
pdf.add_page()
pdf.section_title('6. Email Notification System')
pdf.body_text(
    'The platform uses Gmail SMTP with App Passwords for sending transactional emails. '
    'Emails are sent asynchronously using Spring\'s @Async annotation so they don\'t block the API response.'
)

pdf.sub_title('6.1 Application Confirmation Email')
pdf.body_text('When a student applies to an internship, they receive a professional HTML email containing:')
pdf.bullet('Internship Details - Title, company, location, work mode, stipend, deadline')
pdf.bullet('AI Match Score - Color-coded badge (green >= 75%, orange >= 50%, red < 50%)')
pdf.bullet('Next Steps - Checklist of what to expect after applying')
pdf.bullet('Company Info - Brief about the hiring company')

pdf.sub_title('6.2 OTP Email (Password Reset)')
pdf.body_text(
    'When a user requests a password reset, a 6-digit OTP is sent to their registered email address. '
    'OTPs expire after a configured time period.'
)

pdf.info_box(
    'SMTP Configuration: Gmail SMTP (smtp.gmail.com:587)\n'
    'with TLS encryption and App Password authentication.',
    'green'
)

# =================== 7. TECH STACK ===================
pdf.ln(4)
pdf.section_title('7. Tech Stack & Dependencies')
pdf.add_table(
    ['Layer', 'Technology', 'Version'],
    [
        ['Backend Framework', 'Spring Boot', '3.3.2'],
        ['Language', 'Java', '17 (runs on 21)'],
        ['Database', 'MySQL', '8.0'],
        ['ORM', 'Hibernate / Spring Data JPA', '6.x'],
        ['Security', 'Spring Security + JWT', '6.x'],
        ['Email', 'Spring Boot Mail (Jakarta Mail)', '3.x'],
        ['Build Tool', 'Apache Maven', '3.9.16'],
        ['Frontend', 'HTML / CSS / JavaScript', 'ES6+'],
        ['AI/NLP', 'Custom TF-IDF Engine (Java)', '-'],
        ['API Format', 'RESTful JSON', '-'],
    ],
    [55, 80, 55]
)

# =================== 8. PROJECT STRUCTURE ===================
pdf.add_page()
pdf.section_title('8. Project Structure')
struct = """pm-internship-matcher/
 backend/
  src/main/java/com/internship/matcher/
   MatcherApplication.java        # Main entry (@EnableAsync)
   config/
    SecurityConfig.java           # JWT + CORS security
    CorsConfig.java               # Cross-origin config
   controller/
    AuthController.java           # Login, Register, Forgot PWD
    StudentController.java        # Profile management
    InternshipController.java     # Browse internships
    ApplicationController.java    # Apply to internships
    RecommendationController.java # AI recommendations
   service/
    RecommendationEngine.java     # AI Engine (TF-IDF)
    ApplicationService.java       # App logic + email
    MailService.java              # Email templates
    AuthService.java              # Auth & JWT logic
    StudentService.java           # Student CRUD
   model/
    Student.java, Internship.java, Application.java
   dto/request/ and dto/response/
    RecommendationResponse.java   # AI recommendation DTO
   repository/                    # JPA repositories
  src/main/resources/
   application.properties         # All configuration
 frontend/
  index.html, register.html, dashboard.html
  profile.html, browse.html, recommendations.html
  forgot-password.html
  css/style.css                   # Global styles
  js/api.js, config.js, nav.js, profile.js
 database/
  seed_data.sql                   # 100+ internships seed
 apache-maven-3.9.16/             # Bundled Maven"""
pdf.code_block(struct)

# =================== 9. HOW TO RUN ===================
pdf.add_page()
pdf.section_title('9. How to Run the Project')

pdf.sub_title('Prerequisites')
pdf.bullet('Java 17+ (JDK installed and in PATH)')
pdf.bullet('MySQL 8.0 running on localhost:3306')
pdf.bullet('Database named "internship" created in MySQL')
pdf.bullet('No additional installations needed - Maven is bundled')

pdf.sub_title('Step 1: Create the Database (first time only)')
pdf.code_block('mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS internship;"')

pdf.sub_title('Step 2: Start the Backend Server')
pdf.code_block('cd d:\\Arnav\\project\\pm-internship-matcher\\backend\nd:\\Arnav\\project\\pm-internship-matcher\\apache-maven-3.9.16\\bin\\mvn.cmd spring-boot:run')
pdf.info_box('Wait for: "Started MatcherApplication in X seconds"\nBackend runs on http://localhost:8080')

pdf.sub_title('Step 3: Seed the Database (first time only)')
pdf.body_text('Open a new terminal and run:')
pdf.code_block('Get-Content "d:\\Arnav\\project\\pm-internship-matcher\\database\\seed_data.sql"\n  -Raw | & "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe"\n  -u root -proot internship')

pdf.sub_title('Step 4: Start the Frontend Server')
pdf.body_text('Open another new terminal:')
pdf.code_block('cd d:\\Arnav\\project\\pm-internship-matcher\\frontend\npython -m http.server 5500')
pdf.info_box('Frontend runs on http://localhost:5500', 'green')

pdf.sub_title('Step 5: Open in Browser')
pdf.body_text('Navigate to: http://localhost:5500/index.html')

pdf.sub_title('Complete User Flow')
pdf.bullet('1. Register a new account with your email')
pdf.bullet('2. Login with your credentials')
pdf.bullet('3. Complete your Profile - field of study, qualification, skills, interests, locations')
pdf.bullet('4. View AI Recommendations - engine analyzes your profile and ranks 100+ internships')
pdf.bullet('5. Apply to internships - click "Apply" on any recommendation')
pdf.bullet('6. Check Email - receive a professional HTML confirmation email')
pdf.bullet('7. Track Applications - view all applied internships on the Dashboard')

# =================== 10. API ENDPOINTS ===================
pdf.add_page()
pdf.section_title('10. API Endpoints')
pdf.add_table(
    ['Method', 'Endpoint', 'Description', 'Auth'],
    [
        ['POST', '/api/auth/register', 'Register new student', 'No'],
        ['POST', '/api/auth/login', 'Login and get JWT token', 'No'],
        ['POST', '/api/auth/forgot-password', 'Send OTP to email', 'No'],
        ['POST', '/api/auth/reset-password', 'Reset password with OTP', 'No'],
        ['GET', '/api/students/profile', 'Get student profile', 'JWT'],
        ['PUT', '/api/students/profile', 'Update student profile', 'JWT'],
        ['GET', '/api/internships', 'Browse all internships', 'JWT'],
        ['GET', '/api/internships/{id}', 'Get internship details', 'JWT'],
        ['GET', '/api/recommendations', 'Get AI recommendations', 'JWT'],
        ['POST', '/api/applications/{id}', 'Apply to internship', 'JWT'],
        ['GET', '/api/applications', 'Get my applications', 'JWT'],
    ],
    [20, 55, 75, 40]
)
pdf.info_box(
    'Authentication: All protected endpoints require a JWT token\n'
    'in the Authorization: Bearer <token> header.\n'
    'Tokens are issued on login and expire after the configured duration.'
)

# =================== 11. KEY FILES ===================
pdf.ln(4)
pdf.section_title('11. Key Files Reference')
pdf.add_table(
    ['File', 'Purpose'],
    [
        ['RecommendationEngine.java', 'Core AI engine - TF-IDF, cosine similarity, 7-factor scoring'],
        ['RecommendationResponse.java', 'DTO with match score, description, field score, explanation'],
        ['MailService.java', 'Email service with HTML templates for confirmation & OTP'],
        ['ApplicationService.java', 'Business logic for applying - validates, saves, emails'],
        ['SecurityConfig.java', 'Spring Security - JWT filter, CORS, public/protected routes'],
        ['application.properties', 'All config - database, SMTP, JWT secret, server settings'],
        ['seed_data.sql', 'Database seed - 100+ internships, 92 skills, mappings, tags'],
        ['recommendations.html', 'Frontend AI recommendations with match bars and apply buttons'],
    ],
    [60, 130]
)

# =================== 12. DATABASE SCHEMA ===================
pdf.add_page()
pdf.section_title('12. Database Schema')
pdf.add_table(
    ['Table', 'Key Columns', 'Description'],
    [
        ['students', 'id, name, email, password, field_of_study, qualification, cgpa, bio', 'Registered student profiles'],
        ['internships', 'id, title, company_name, sector, description, location, stipend', 'Available internship listings'],
        ['applications', 'id, student_id, internship_id, status, match_score', 'Student applications'],
        ['skills', 'id, name, category', 'Master skills catalog (92 skills)'],
        ['student_skills', 'student_id, skill_id, proficiency', 'Student-skill mapping'],
        ['internship_skills', 'internship_id, skill_id, importance', 'Required skills per internship'],
        ['student_interests', 'student_id, interest', 'Student interest tags'],
        ['internship_interest_tags', 'internship_id, tag', 'Internship domain tags'],
        ['student_preferred_locations', 'student_id, location', 'Preferred work locations'],
    ],
    [52, 90, 48]
)

pdf.sub_title('Entity Relationships')
pdf.code_block(
    'Student  -- 1:N --> Applications <-- N:1 -- Internship\n'
    '   |                                           |\n'
    '   +-- N:M --> Student_Skills                  +-- 1:N --> Internship_Skills\n'
    '   |          (proficiency)                    |          (importance)\n'
    '   +-- 1:N --> Student_Interests               +-- 1:N --> Interest_Tags\n'
    '   |                                           |\n'
    '   +-- 1:N --> Preferred_Locations              +-- N:1 --> Skills (master)'
)

# Save
output_path = r'd:\Arnav\project\pm-internship-matcher\PM-Internship-Matcher-Project-Guide.pdf'
pdf.output(output_path)
print(f'PDF created successfully at: {output_path}')
