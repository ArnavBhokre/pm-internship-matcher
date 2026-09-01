-- =====================================================================
--  Sample seed data for local testing / demo.
--  Run this AFTER the backend has started once (so tables exist), OR
--  after running internship_schema.sql manually.
--  Usage:  mysql -u root -p internship < seed_data.sql
-- =====================================================================

USE internship;

INSERT INTO skills (name, category) VALUES
('Java', 'Programming'), ('Python', 'Programming'), ('SQL', 'Data'),
('Excel', 'Data'), ('Communication', 'Soft Skill'), ('Content Writing', 'Communication'),
('Digital Marketing', 'Marketing'), ('Graphic Design', 'Design'), ('Data Analysis', 'Data'),
('Public Speaking', 'Soft Skill'), ('React', 'Programming'), ('Accounting', 'Finance'),
('Social Media Management', 'Marketing'), ('Research', 'Analytical'), ('MS Office', 'General')
ON DUPLICATE KEY UPDATE name = name;

INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at)
VALUES
('Data Analytics Intern', 'Ministry of Statistics', 'Government', 'Assist in analyzing public survey datasets and preparing dashboards for policy decisions.', 'New Delhi', 'ONSITE', '6 months', 10000, 'Any Graduate', 20, '2026-10-15', TRUE, NOW()),
('Software Development Intern', 'National Informatics Centre', 'IT', 'Work on e-governance web applications using Java Spring Boot and MySQL.', 'Bengaluru', 'HYBRID', '6 months', 15000, 'B.Tech/B.E./MCA', 15, '2026-10-01', TRUE, NOW()),
('Digital Marketing Intern', 'Ministry of Tourism', 'Marketing', 'Support social media campaigns promoting tourism initiatives across India.', 'Mumbai', 'REMOTE', '3 months', 8000, 'Any Graduate', 25, '2026-09-30', TRUE, NOW()),
('Finance & Accounts Intern', 'Public Sector Bank', 'Finance', 'Assist the finance team with reconciliation, reporting and Excel-based analysis.', 'Chennai', 'ONSITE', '6 months', 9000, 'B.Com/BBA/MBA', 10, '2026-10-20', TRUE, NOW()),
('Content Writing Intern', 'Ministry of Information & Broadcasting', 'Media', 'Draft articles, press notes and social copy on government schemes.', 'New Delhi', 'REMOTE', '3 months', 7000, 'Any Graduate', 12, '2026-09-25', TRUE, NOW()),
('UI/UX Design Intern', 'Digital India Corporation', 'IT', 'Design user interfaces for citizen-facing government portals.', 'Hyderabad', 'HYBRID', '6 months', 12000, 'Any Graduate', 8, '2026-10-10', TRUE, NOW()),
('Research Intern - Rural Development', 'Ministry of Rural Development', 'Government', 'Support field research and data collection for rural livelihood schemes.', 'Jaipur', 'ONSITE', '6 months', 8500, 'Any Graduate', 15, '2026-11-01', TRUE, NOW()),
('Data Science Intern', 'NITI Aayog', 'IT', 'Build predictive models on public welfare datasets using Python.', 'New Delhi', 'HYBRID', '6 months', 16000, 'B.Tech/B.E./M.Sc', 6, '2026-10-05', TRUE, NOW());

-- Skill requirements for each internship (adjust IDs if you already had skills before seeding)
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Data Analytics Intern' AND s.name='Data Analysis';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Data Analytics Intern' AND s.name='Excel';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 3 FROM internships i, skills s WHERE i.title='Data Analytics Intern' AND s.name='SQL';

INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Software Development Intern' AND s.name='Java';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Software Development Intern' AND s.name='SQL';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 3 FROM internships i, skills s WHERE i.title='Software Development Intern' AND s.name='React';

INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Digital Marketing Intern' AND s.name='Digital Marketing';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Digital Marketing Intern' AND s.name='Social Media Management';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 3 FROM internships i, skills s WHERE i.title='Digital Marketing Intern' AND s.name='Content Writing';

INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Finance & Accounts Intern' AND s.name='Accounting';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Finance & Accounts Intern' AND s.name='Excel';

INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Content Writing Intern' AND s.name='Content Writing';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 3 FROM internships i, skills s WHERE i.title='Content Writing Intern' AND s.name='Communication';

INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='UI/UX Design Intern' AND s.name='Graphic Design';

INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Research Intern - Rural Development' AND s.name='Research';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 3 FROM internships i, skills s WHERE i.title='Research Intern - Rural Development' AND s.name='Communication';

INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Data Science Intern' AND s.name='Python';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Data Science Intern' AND s.name='Data Analysis';
INSERT INTO internship_skills (internship_id, skill_id, importance)
SELECT i.id, s.id, 3 FROM internships i, skills s WHERE i.title='Data Science Intern' AND s.name='SQL';

-- Interest tags per internship
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'data' FROM internships WHERE title='Data Analytics Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'analytics' FROM internships WHERE title='Data Analytics Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'software' FROM internships WHERE title='Software Development Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'coding' FROM internships WHERE title='Software Development Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'marketing' FROM internships WHERE title='Digital Marketing Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'social media' FROM internships WHERE title='Digital Marketing Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'finance' FROM internships WHERE title='Finance & Accounts Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'writing' FROM internships WHERE title='Content Writing Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'design' FROM internships WHERE title='UI/UX Design Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'research' FROM internships WHERE title='Research Intern - Rural Development';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'data science' FROM internships WHERE title='Data Science Intern';
INSERT INTO internship_interest_tags (internship_id, tag) SELECT id, 'ai' FROM internships WHERE title='Data Science Intern';
