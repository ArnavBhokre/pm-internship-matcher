-- =====================================================================
--  Comprehensive seed data covering ALL education fields.
--  Run AFTER the backend has started once (so tables exist).
-- =====================================================================

USE internship;

-- Make organization nullable temporarily for seeding
ALTER TABLE internships MODIFY organization VARCHAR(255) DEFAULT '';

-- =====================================================================
--  SKILLS (70+ covering all domains)
-- =====================================================================
INSERT INTO skills (name, category) VALUES
('Java', 'Programming'), ('Python', 'Programming'), ('JavaScript', 'Programming'),
('React', 'Programming'), ('Node.js', 'Programming'), ('SQL', 'Data'),
('HTML/CSS', 'Programming'), ('C++', 'Programming'), ('C#', 'Programming'),
('PHP', 'Programming'), ('Swift', 'Programming'), ('Kotlin', 'Programming'),
('TypeScript', 'Programming'), ('Angular', 'Programming'), ('Spring Boot', 'Programming'),
('Machine Learning', 'Data Science'), ('Data Analysis', 'Data'), ('Deep Learning', 'Data Science'),
('Cloud Computing', 'IT'), ('DevOps', 'IT'), ('Cybersecurity', 'IT'),
('Artificial Intelligence', 'Data Science'),
('Excel', 'Data'), ('Tableau', 'Data'), ('Power BI', 'Data'),
('R Programming', 'Data'), ('Statistics', 'Data'), ('Big Data', 'Data'),
('Graphic Design', 'Design'), ('UI/UX Design', 'Design'), ('Figma', 'Design'),
('Adobe Photoshop', 'Design'), ('AutoCAD', 'Design'), ('3D Modeling', 'Design'),
('Fashion Design', 'Design'), ('Interior Design', 'Design'),
('Accounting', 'Finance'), ('Financial Analysis', 'Finance'), ('Tally', 'Finance'),
('GST/Taxation', 'Finance'), ('Auditing', 'Finance'), ('Investment Analysis', 'Finance'),
('Digital Marketing', 'Marketing'), ('SEO/SEM', 'Marketing'), ('Social Media Management', 'Marketing'),
('Content Writing', 'Communication'), ('Copywriting', 'Communication'),
('Communication', 'Soft Skill'), ('Public Speaking', 'Soft Skill'),
('MATLAB', 'Engineering'), ('SolidWorks', 'Engineering'), ('Circuit Design', 'Engineering'),
('Embedded Systems', 'Engineering'), ('PLC Programming', 'Engineering'),
('Structural Analysis', 'Engineering'), ('GIS Mapping', 'Engineering'),
('Lab Techniques', 'Science'), ('Research Methodology', 'Science'),
('Bioinformatics', 'Science'), ('Spectroscopy', 'Science'), ('Statistical Analysis', 'Science'),
('Legal Research', 'Legal'), ('Contract Drafting', 'Legal'), ('Legal Writing', 'Legal'),
('Clinical Research', 'Healthcare'), ('Patient Care', 'Healthcare'),
('Medical Documentation', 'Healthcare'), ('Pharmacovigilance', 'Healthcare'),
('MS Office', 'General'), ('Project Management', 'General'),
('Team Leadership', 'Soft Skill'), ('Problem Solving', 'Soft Skill'),
('Critical Thinking', 'Soft Skill'), ('Event Management', 'General'),
('Teaching', 'Education'), ('Curriculum Development', 'Education'),
('Photography', 'Creative'), ('Video Editing', 'Creative'),
('Supply Chain', 'Operations'), ('Quality Assurance', 'Operations'),
('Customer Service', 'Service'), ('Hospitality Management', 'Service'),
('Agricultural Science', 'Agriculture'), ('Crop Management', 'Agriculture'),
('Environmental Assessment', 'Environment'), ('Sustainability Planning', 'Environment')
ON DUPLICATE KEY UPDATE name = name;

-- =====================================================================
--  INTERNSHIPS (80+ across all education fields)
-- =====================================================================

-- CS / IT
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Software Development Intern', 'National Informatics Centre', 'IT', 'Work on e-governance web applications using Java Spring Boot and MySQL.', 'Bengaluru', 'HYBRID', '6 months', 15000, 'B.Tech/B.E./MCA', 15, '2027-03-01', TRUE, NOW()),
('Data Science Intern', 'NITI Aayog', 'IT', 'Build predictive models on public welfare datasets using Python and TensorFlow.', 'New Delhi', 'HYBRID', '6 months', 16000, 'B.Tech/B.E./M.Sc', 6, '2027-03-15', TRUE, NOW()),
('Full Stack Web Developer Intern', 'TCS Digital', 'IT', 'Develop enterprise web applications using React, Node.js and MongoDB.', 'Pune', 'ONSITE', '6 months', 18000, 'B.Tech/B.E./MCA', 20, '2027-02-28', TRUE, NOW()),
('AI/ML Research Intern', 'Indian Institute of Science', 'Research', 'Conduct research on NLP and computer vision models. Publish findings.', 'Bengaluru', 'ONSITE', '6 months', 20000, 'B.Tech/M.Tech/M.Sc', 5, '2027-04-01', TRUE, NOW()),
('Cybersecurity Intern', 'CERT-In', 'IT', 'Assist in vulnerability assessment and penetration testing of government infrastructure.', 'New Delhi', 'ONSITE', '6 months', 15000, 'B.Tech/B.E./MCA', 8, '2027-03-10', TRUE, NOW()),
('Cloud Computing Intern', 'Infosys', 'IT', 'Work with AWS and Azure cloud platforms to build scalable microservices.', 'Mysuru', 'HYBRID', '6 months', 17000, 'B.Tech/B.E./MCA', 12, '2027-03-20', TRUE, NOW()),
('Mobile App Development Intern', 'Paytm', 'IT', 'Develop features for Android and iOS mobile applications using Kotlin and Swift.', 'Noida', 'ONSITE', '6 months', 20000, 'B.Tech/B.E./MCA', 10, '2027-02-15', TRUE, NOW()),
('DevOps Engineering Intern', 'Wipro', 'IT', 'Automate deployment pipelines and manage cloud infrastructure using Jenkins and Terraform.', 'Hyderabad', 'HYBRID', '6 months', 16000, 'B.Tech/B.E.', 8, '2027-03-25', TRUE, NOW()),
('Database Administration Intern', 'Oracle India', 'IT', 'Manage enterprise databases, write complex SQL queries and performance tuning.', 'Bengaluru', 'ONSITE', '6 months', 18000, 'B.Tech/B.E./MCA', 6, '2027-04-10', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Electronics & Communication
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Embedded Systems Intern', 'Bharat Electronics Limited', 'Electronics', 'Design embedded firmware for defense communication systems using ARM microcontrollers.', 'Bengaluru', 'ONSITE', '6 months', 14000, 'B.Tech/B.E. (ECE/EEE)', 10, '2027-03-01', TRUE, NOW()),
('IoT Solutions Intern', 'Bosch India', 'Electronics', 'Build IoT prototypes for smart home and industrial automation using sensors and Raspberry Pi.', 'Bengaluru', 'HYBRID', '6 months', 16000, 'B.Tech/B.E. (ECE/CS)', 8, '2027-03-15', TRUE, NOW()),
('VLSI Design Intern', 'Intel India', 'Semiconductor', 'Assist in VLSI chip design verification using Verilog and SystemVerilog.', 'Bengaluru', 'ONSITE', '6 months', 22000, 'B.Tech/M.Tech (ECE/VLSI)', 5, '2027-04-01', TRUE, NOW()),
('Telecom Network Intern', 'Jio Platforms', 'Telecom', 'Support 5G network planning and optimization. Analyze network performance data.', 'Mumbai', 'ONSITE', '6 months', 15000, 'B.Tech/B.E. (ECE)', 12, '2027-03-10', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Mechanical Engineering
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Mechanical Design Intern', 'Tata Motors', 'Automotive', 'Create CAD models using SolidWorks for automotive components and prototype testing.', 'Pune', 'ONSITE', '6 months', 15000, 'B.Tech/B.E. (Mech)', 10, '2027-03-01', TRUE, NOW()),
('Manufacturing Process Intern', 'Maruti Suzuki', 'Automotive', 'Support lean manufacturing and process optimization on the production floor.', 'Gurugram', 'ONSITE', '6 months', 14000, 'B.Tech/B.E. (Mech)', 15, '2027-02-28', TRUE, NOW()),
('Robotics Intern', 'DRDO', 'Defense', 'Build robotic systems for defense applications with sensors and actuators.', 'Hyderabad', 'ONSITE', '6 months', 16000, 'B.Tech/M.Tech (Mech/ECE)', 6, '2027-04-15', TRUE, NOW()),
('Energy Engineering Intern', 'NTPC', 'Energy', 'Support thermal power plant operations and renewable energy feasibility studies.', 'Noida', 'ONSITE', '6 months', 13000, 'B.Tech/B.E. (Mech/Energy)', 8, '2027-03-20', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Civil Engineering
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Structural Engineering Intern', 'L&T Construction', 'Construction', 'Structural analysis using STAAD Pro and ETABS. Prepare design drawings.', 'Mumbai', 'ONSITE', '6 months', 14000, 'B.Tech/B.E. (Civil)', 10, '2027-03-01', TRUE, NOW()),
('Urban Planning Intern', 'Delhi Development Authority', 'Government', 'Support urban development planning, land use analysis, and GIS-based mapping.', 'New Delhi', 'ONSITE', '6 months', 12000, 'B.Tech/B.Planning/B.Arch', 8, '2027-03-15', TRUE, NOW()),
('Environmental Impact Intern', 'Ministry of Environment', 'Government', 'Conduct environmental impact assessments and analyze pollution monitoring data.', 'New Delhi', 'HYBRID', '6 months', 11000, 'B.Tech (Civil/Environmental)', 6, '2027-04-01', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Chemical Engineering
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Chemical Process Intern', 'Reliance Industries', 'Chemical', 'Assist in petrochemical plant operations and process optimization.', 'Jamnagar', 'ONSITE', '6 months', 15000, 'B.Tech/B.E. (Chemical)', 8, '2027-03-10', TRUE, NOW()),
('Quality Control Intern', 'Hindustan Unilever', 'Manufacturing', 'Conduct quality tests on FMCG products and support Six Sigma initiatives.', 'Mumbai', 'ONSITE', '6 months', 13000, 'B.Tech/B.Sc (Chemistry/Chemical)', 10, '2027-02-28', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Data Analytics
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Data Analytics Intern', 'Ministry of Statistics', 'Government', 'Analyze public survey datasets, build dashboards using Tableau and Power BI.', 'New Delhi', 'ONSITE', '6 months', 10000, 'Any Graduate', 20, '2027-03-01', TRUE, NOW()),
('Business Intelligence Intern', 'Flipkart', 'E-Commerce', 'Create data dashboards and perform cohort analysis for marketplace insights.', 'Bengaluru', 'HYBRID', '6 months', 18000, 'B.Tech/MBA/B.Sc', 8, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Commerce & Finance
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Finance & Accounts Intern', 'State Bank of India', 'Banking', 'Assist in financial analysis, account reconciliation, and GST filing.', 'Mumbai', 'ONSITE', '6 months', 12000, 'B.Com/BBA/MBA', 15, '2027-03-01', TRUE, NOW()),
('Audit & Compliance Intern', 'Deloitte India', 'Consulting', 'Support statutory and internal audit engagements and prepare audit reports.', 'Mumbai', 'ONSITE', '6 months', 18000, 'B.Com/CA Inter/MBA', 10, '2027-02-15', TRUE, NOW()),
('Taxation Intern', 'PwC India', 'Consulting', 'Assist in direct and indirect tax compliance and GST return filing.', 'Delhi', 'HYBRID', '6 months', 17000, 'B.Com/CA Inter/LLB', 8, '2027-03-10', TRUE, NOW()),
('Banking Operations Intern', 'HDFC Bank', 'Banking', 'Support retail banking operations, loan processing, and financial product analysis.', 'Mumbai', 'ONSITE', '6 months', 14000, 'B.Com/BBA/MBA', 12, '2027-03-20', TRUE, NOW()),
('Investment Research Intern', 'ICICI Securities', 'Finance', 'Conduct equity research, analyze company financials, and prepare industry reports.', 'Mumbai', 'HYBRID', '6 months', 16000, 'B.Com/MBA (Finance)', 6, '2027-04-01', TRUE, NOW()),
('FinTech Product Intern', 'PhonePe', 'FinTech', 'Contribute to digital payment solutions and analyze transaction data.', 'Bengaluru', 'HYBRID', '6 months', 20000, 'B.Tech/MBA/B.Com', 8, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Marketing & Advertising
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Digital Marketing Intern', 'Ministry of Tourism', 'Marketing', 'Support social media campaigns, run Google Ads, and analyze engagement metrics.', 'Mumbai', 'REMOTE', '3 months', 8000, 'Any Graduate', 25, '2027-03-01', TRUE, NOW()),
('Brand Management Intern', 'ITC Limited', 'FMCG', 'Assist in brand strategy, consumer research, and launch planning for FMCG products.', 'Kolkata', 'ONSITE', '6 months', 15000, 'MBA/BBA', 8, '2027-03-10', TRUE, NOW()),
('SEO & Content Marketing Intern', 'Zomato', 'Technology', 'Optimize website content for search engines and develop content strategies.', 'Gurugram', 'HYBRID', '3 months', 12000, 'Any Graduate', 10, '2027-02-28', TRUE, NOW()),
('Market Research Intern', 'Nielsen India', 'Research', 'Conduct primary and secondary market research and analyze consumer behavior data.', 'Mumbai', 'ONSITE', '6 months', 14000, 'MBA/B.Com/BA', 10, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Human Resources
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('HR Operations Intern', 'Accenture India', 'Consulting', 'Support recruitment processes, employee onboarding, and HR data analytics.', 'Bengaluru', 'HYBRID', '6 months', 14000, 'MBA (HR)/BBA', 10, '2027-03-01', TRUE, NOW()),
('Talent Acquisition Intern', 'Amazon India', 'Technology', 'Assist in candidate sourcing, screening, and recruitment analytics.', 'Hyderabad', 'ONSITE', '6 months', 16000, 'MBA (HR)/Any Graduate', 8, '2027-03-20', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Media & Journalism
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Content Writing Intern', 'Ministry of Information', 'Media', 'Draft articles, press notes, and social media copy on government schemes.', 'New Delhi', 'REMOTE', '3 months', 7000, 'Any Graduate', 12, '2027-03-01', TRUE, NOW()),
('Journalism Intern', 'The Hindu', 'Media', 'Report on current affairs, conduct interviews, and write news articles.', 'Chennai', 'ONSITE', '6 months', 10000, 'BA (Journalism)/Any Graduate', 6, '2027-03-10', TRUE, NOW()),
('Video Production Intern', 'NDTV', 'Media', 'Assist in video shooting, editing, and post-production using Adobe Premiere.', 'New Delhi', 'ONSITE', '6 months', 12000, 'Any Graduate', 5, '2027-02-28', TRUE, NOW()),
('Public Relations Intern', 'Edelman India', 'Communication', 'Draft press releases, manage media relations, and support PR campaigns.', 'Mumbai', 'HYBRID', '3 months', 13000, 'BA (Mass Comm)/MBA', 8, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Law
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Legal Research Intern', 'Supreme Court of India', 'Legal', 'Conduct legal research on constitutional matters and prepare case briefs.', 'New Delhi', 'ONSITE', '6 months', 10000, 'LLB/BA LLB', 8, '2027-03-01', TRUE, NOW()),
('Corporate Law Intern', 'AZB & Partners', 'Legal', 'Assist in M&A transactions, due diligence, and contract drafting.', 'Mumbai', 'ONSITE', '6 months', 15000, 'LLB/BA LLB', 6, '2027-03-10', TRUE, NOW()),
('Intellectual Property Intern', 'Patent Office India', 'Legal', 'Review patent applications and conduct prior art searches.', 'Delhi', 'ONSITE', '6 months', 11000, 'LLB/B.Tech + LLB', 5, '2027-03-15', TRUE, NOW()),
('Policy Research Intern', 'PRS Legislative Research', 'Government', 'Analyze parliamentary bills and prepare policy briefs.', 'New Delhi', 'HYBRID', '6 months', 12000, 'LLB/BA (Political Science)/MPP', 8, '2027-04-01', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Medicine & Healthcare
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Clinical Research Intern', 'AIIMS', 'Healthcare', 'Assist in clinical trial coordination, patient data collection, and research papers.', 'New Delhi', 'ONSITE', '6 months', 12000, 'MBBS/B.Pharma/M.Sc', 8, '2027-03-01', TRUE, NOW()),
('Public Health Intern', 'WHO India Office', 'Healthcare', 'Support public health surveillance and community health program evaluation.', 'New Delhi', 'HYBRID', '6 months', 15000, 'MBBS/MPH/B.Sc (Nursing)', 6, '2027-03-15', TRUE, NOW()),
('Hospital Administration Intern', 'Apollo Hospitals', 'Healthcare', 'Assist in hospital operations, patient flow analysis, and resource planning.', 'Chennai', 'ONSITE', '6 months', 13000, 'BHA/MBA (Healthcare)/Any Graduate', 10, '2027-03-10', TRUE, NOW()),
('Medical Writing Intern', 'Dr. Reddy''s Laboratories', 'Pharmaceutical', 'Prepare clinical study reports and regulatory documents.', 'Hyderabad', 'HYBRID', '6 months', 14000, 'B.Pharma/MBBS/M.Sc', 5, '2027-04-01', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Pharmacy
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Pharmaceutical Research Intern', 'Sun Pharma', 'Pharmaceutical', 'Conduct drug formulation experiments and analytical method development.', 'Mumbai', 'ONSITE', '6 months', 14000, 'B.Pharma/M.Pharma', 10, '2027-03-01', TRUE, NOW()),
('Pharmacovigilance Intern', 'Cipla', 'Pharmaceutical', 'Monitor adverse drug reactions and maintain pharmacovigilance databases.', 'Mumbai', 'HYBRID', '6 months', 13000, 'B.Pharma/M.Pharma', 8, '2027-03-10', TRUE, NOW()),
('Regulatory Affairs Intern', 'Biocon', 'Pharmaceutical', 'Assist in preparing regulatory dossiers for drug approvals.', 'Bengaluru', 'ONSITE', '6 months', 15000, 'B.Pharma/M.Pharma', 6, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Biotechnology
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Biotechnology Research Intern', 'DBT-ICT Centre', 'Biotechnology', 'Conduct experiments in molecular biology and bioprocess optimization.', 'Mumbai', 'ONSITE', '6 months', 14000, 'B.Tech (Biotech)/M.Sc', 8, '2027-03-01', TRUE, NOW()),
('Bioinformatics Intern', 'CSIR-IGIB', 'Biotechnology', 'Analyze genomic data using bioinformatics tools and develop pipelines.', 'New Delhi', 'HYBRID', '6 months', 15000, 'B.Tech/M.Sc (Bioinformatics)', 5, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Agriculture
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Agricultural Research Intern', 'ICAR', 'Agriculture', 'Support field trials for new crop varieties and analyze yield parameters.', 'New Delhi', 'ONSITE', '6 months', 10000, 'B.Sc (Agriculture)/B.Tech (Agri)', 12, '2027-03-01', TRUE, NOW()),
('AgriTech Intern', 'CropIn Technology', 'AgriTech', 'Develop precision agriculture solutions and analyze satellite imagery for crop monitoring.', 'Bengaluru', 'HYBRID', '6 months', 15000, 'B.Tech/B.Sc (Agriculture/CS)', 8, '2027-03-10', TRUE, NOW()),
('Food Science Intern', 'FSSAI', 'Government', 'Assist in food safety testing and quality standard compliance.', 'New Delhi', 'ONSITE', '6 months', 11000, 'B.Sc (Food Tech)/B.Tech (Food)', 10, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Environmental Science
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Environmental Research Intern', 'TERI', 'Environment', 'Conduct climate change research, carbon footprint analysis, and sustainability reporting.', 'New Delhi', 'HYBRID', '6 months', 12000, 'B.Sc/M.Sc (Environmental Science)', 8, '2027-03-01', TRUE, NOW()),
('Sustainability Intern', 'Tata Sustainability Group', 'Corporate', 'Support ESG reporting and circular economy initiatives.', 'Mumbai', 'HYBRID', '6 months', 14000, 'Any Graduate', 10, '2027-03-15', TRUE, NOW()),
('Wildlife Conservation Intern', 'WWF India', 'NGO', 'Support wildlife habitat monitoring and biodiversity surveys.', 'New Delhi', 'ONSITE', '6 months', 10000, 'B.Sc/M.Sc (Zoology/Environmental)', 6, '2027-04-01', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Physics & Mathematics
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Physics Research Intern', 'ISRO', 'Space', 'Assist in satellite instrumentation testing and computational physics simulations.', 'Bengaluru', 'ONSITE', '6 months', 16000, 'B.Sc/M.Sc (Physics)/B.Tech', 5, '2027-03-01', TRUE, NOW()),
('Actuarial Analysis Intern', 'LIC of India', 'Insurance', 'Assist in actuarial modeling, mortality analysis, and premium calculation.', 'Mumbai', 'ONSITE', '6 months', 14000, 'B.Sc (Mathematics/Statistics)', 8, '2027-03-10', TRUE, NOW()),
('Quantitative Research Intern', 'Goldman Sachs India', 'Finance', 'Develop quantitative models for trading strategies and pricing algorithms.', 'Bengaluru', 'ONSITE', '6 months', 25000, 'B.Sc/M.Sc (Math/Physics/Stats)', 5, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Chemistry & Biology
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Analytical Chemistry Intern', 'Indian Oil Corporation', 'Energy', 'Conduct chemical analysis of petroleum products using HPLC and GC-MS.', 'Faridabad', 'ONSITE', '6 months', 13000, 'B.Sc/M.Sc (Chemistry)', 8, '2027-03-01', TRUE, NOW()),
('Materials Science Intern', 'CSIR-NPL', 'Research', 'Support research in nanomaterials characterization and advanced materials testing.', 'New Delhi', 'ONSITE', '6 months', 14000, 'B.Sc/M.Sc (Chemistry/Physics)', 6, '2027-03-15', TRUE, NOW()),
('Microbiology Research Intern', 'ICMR', 'Healthcare', 'Conduct microbiological testing and support infectious disease research.', 'New Delhi', 'ONSITE', '6 months', 12000, 'B.Sc/M.Sc (Microbiology/Biology)', 8, '2027-03-01', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Psychology
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Counseling Psychology Intern', 'Vandrevala Foundation', 'Healthcare', 'Provide counseling support under supervision and conduct psychological assessments.', 'Mumbai', 'HYBRID', '6 months', 10000, 'BA/MA (Psychology)', 6, '2027-03-01', TRUE, NOW()),
('Organizational Psychology Intern', 'Mercer India', 'Consulting', 'Support employee engagement surveys and organizational development initiatives.', 'Gurugram', 'HYBRID', '6 months', 15000, 'MA (Psychology)/MBA (HR)', 5, '2027-03-10', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Sociology & Political Science
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Social Research Intern', 'Ministry of Rural Development', 'Government', 'Support field research and data collection for rural livelihood schemes.', 'New Delhi', 'ONSITE', '6 months', 9000, 'BA/MA (Sociology/Social Work)', 15, '2027-03-01', TRUE, NOW()),
('NGO Program Intern', 'Pratham Education Foundation', 'NGO', 'Assist in education program implementation and impact measurement.', 'Mumbai', 'ONSITE', '6 months', 8000, 'BA/MA (Sociology/Education)', 12, '2027-03-10', TRUE, NOW()),
('Public Policy Intern', 'Observer Research Foundation', 'Think Tank', 'Research and write policy briefs on governance and economic policy.', 'New Delhi', 'HYBRID', '6 months', 13000, 'BA/MA (Political Science/IR)', 8, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Education
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('EdTech Content Intern', 'BYJU''S', 'EdTech', 'Create educational content and design interactive learning modules.', 'Bengaluru', 'REMOTE', '3 months', 12000, 'B.Ed/Any Graduate', 15, '2027-03-01', TRUE, NOW()),
('Teacher Training Intern', 'Teach For India', 'Education', 'Support classroom teaching and develop lesson plans in underprivileged schools.', 'Mumbai', 'ONSITE', '6 months', 8000, 'B.Ed/BA/B.Sc', 20, '2027-02-28', TRUE, NOW()),
('Curriculum Design Intern', 'NCERT', 'Government', 'Assist in designing curriculum materials and creating assessment frameworks.', 'New Delhi', 'ONSITE', '6 months', 10000, 'B.Ed/M.Ed/MA (Education)', 6, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Design
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('UI/UX Design Intern', 'Digital India Corporation', 'IT', 'Design user interfaces for citizen-facing government portals using Figma.', 'Hyderabad', 'HYBRID', '6 months', 12000, 'Any Graduate', 8, '2027-03-01', TRUE, NOW()),
('Graphic Design Intern', 'Ogilvy India', 'Advertising', 'Create visual content for advertising campaigns using Adobe Creative Suite.', 'Mumbai', 'ONSITE', '3 months', 12000, 'BDes/B.Fine Arts/Any Graduate', 10, '2027-02-28', TRUE, NOW()),
('Fashion Design Intern', 'Myntra', 'Fashion', 'Assist in trend research, mood boards, garment sketching, and fashion styling.', 'Bengaluru', 'ONSITE', '6 months', 14000, 'BDes (Fashion)/B.Sc (Fashion)', 6, '2027-03-10', TRUE, NOW()),
('Interior Design Intern', 'Livspace', 'Design', 'Create 3D visualizations and assist in space planning for home interiors.', 'Bengaluru', 'ONSITE', '6 months', 13000, 'BDes (Interior)/B.Arch', 8, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Architecture
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Architecture Intern', 'Hafeez Contractor', 'Architecture', 'Assist in architectural design, 3D modeling, and construction drawings.', 'Mumbai', 'ONSITE', '6 months', 12000, 'B.Arch', 6, '2027-03-01', TRUE, NOW()),
('Sustainable Architecture Intern', 'Green Building Council', 'Architecture', 'Support green building certification and sustainable design consulting.', 'Hyderabad', 'HYBRID', '6 months', 14000, 'B.Arch/M.Arch', 5, '2027-03-10', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Hospitality & Tourism
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Hotel Management Intern', 'Taj Hotels', 'Hospitality', 'Rotate through front office, F&B, housekeeping, and events departments.', 'Mumbai', 'ONSITE', '6 months', 10000, 'BHM/BHMCT', 15, '2027-03-01', TRUE, NOW()),
('Tourism Planning Intern', 'Ministry of Tourism', 'Government', 'Support tourism promotion campaigns and destination development research.', 'New Delhi', 'HYBRID', '3 months', 9000, 'BBA (Tourism)/BHM/Any Graduate', 10, '2027-03-10', TRUE, NOW()),
('Event Management Intern', 'Wizcraft International', 'Events', 'Coordinate logistics for large-scale events and manage vendor relationships.', 'Mumbai', 'ONSITE', '3 months', 11000, 'Any Graduate', 8, '2027-02-28', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Supply Chain & Operations
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Supply Chain Intern', 'Amazon India', 'E-Commerce', 'Optimize warehouse operations and support demand forecasting.', 'Hyderabad', 'ONSITE', '6 months', 18000, 'B.Tech/MBA (Operations)', 10, '2027-03-01', TRUE, NOW()),
('Operations Management Intern', 'Swiggy', 'Technology', 'Analyze delivery operations data and optimize rider allocation algorithms.', 'Bengaluru', 'HYBRID', '6 months', 16000, 'B.Tech/MBA/Any Graduate', 8, '2027-03-10', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- English & Literature
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Editorial Intern', 'Penguin Random House India', 'Publishing', 'Assist in manuscript editing, proofreading, and editorial research.', 'New Delhi', 'HYBRID', '6 months', 10000, 'BA/MA (English/Literature)', 6, '2027-03-01', TRUE, NOW()),
('Technical Writing Intern', 'Google India', 'Technology', 'Write developer documentation, API guides, tutorials, and user manuals.', 'Bengaluru', 'HYBRID', '6 months', 20000, 'BA (English)/B.Tech/Any Graduate', 5, '2027-03-15', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- History & Heritage
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Heritage Conservation Intern', 'Archaeological Survey of India', 'Government', 'Document historical monuments, catalog artifacts, and prepare heritage reports.', 'New Delhi', 'ONSITE', '6 months', 9000, 'BA/MA (History/Archaeology)', 8, '2027-03-01', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Veterinary Science
INSERT INTO internships (title, company_name, sector, description, location, work_mode, duration, stipend_per_month, minimum_qualification, capacity, application_deadline, active, created_at) VALUES
('Veterinary Research Intern', 'IVRI', 'Research', 'Assist in animal health research, disease diagnostics, and vaccine development.', 'Bareilly', 'ONSITE', '6 months', 11000, 'B.V.Sc/M.V.Sc', 6, '2027-03-01', TRUE, NOW()),
('Dairy Science Intern', 'Amul (GCMMF)', 'Dairy', 'Support dairy product quality testing and supply chain coordination.', 'Anand', 'ONSITE', '6 months', 10000, 'B.V.Sc/B.Tech (Dairy/Food)', 8, '2027-03-10', TRUE, NOW())
ON DUPLICATE KEY UPDATE title = title;

-- Set organization = company_name for all new rows
UPDATE internships SET organization = company_name WHERE organization = '' OR organization IS NULL;

-- =====================================================================
--  SKILL MAPPINGS (key internships)
-- =====================================================================
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Software Development Intern' AND i.company_name='National Informatics Centre' AND s.name='Java';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Software Development Intern' AND i.company_name='National Informatics Centre' AND s.name='SQL';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Software Development Intern' AND i.company_name='National Informatics Centre' AND s.name='Spring Boot';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 3 FROM internships i, skills s WHERE i.title='Software Development Intern' AND i.company_name='National Informatics Centre' AND s.name='React';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Data Science Intern' AND s.name='Python';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Data Science Intern' AND s.name='Machine Learning';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Data Science Intern' AND s.name='Data Analysis';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Full Stack Web Developer Intern' AND s.name='React';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Full Stack Web Developer Intern' AND s.name='Node.js';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Full Stack Web Developer Intern' AND s.name='JavaScript';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='AI/ML Research Intern' AND s.name='Machine Learning';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='AI/ML Research Intern' AND s.name='Python';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='AI/ML Research Intern' AND s.name='Deep Learning';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Cybersecurity Intern' AND s.name='Cybersecurity';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Cybersecurity Intern' AND s.name='Python';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Data Analytics Intern' AND s.name='Data Analysis';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Data Analytics Intern' AND s.name='Excel';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Data Analytics Intern' AND s.name='Tableau';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 3 FROM internships i, skills s WHERE i.title='Data Analytics Intern' AND s.name='SQL';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Digital Marketing Intern' AND s.name='Digital Marketing';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Digital Marketing Intern' AND s.name='Social Media Management';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Digital Marketing Intern' AND s.name='SEO/SEM';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Finance & Accounts Intern' AND s.name='Accounting';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Finance & Accounts Intern' AND s.name='Excel';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 3 FROM internships i, skills s WHERE i.title='Finance & Accounts Intern' AND s.name='Tally';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Content Writing Intern' AND s.name='Content Writing';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Content Writing Intern' AND s.name='Communication';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='UI/UX Design Intern' AND s.name='UI/UX Design';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='UI/UX Design Intern' AND s.name='Figma';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Legal Research Intern' AND s.name='Legal Research';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Legal Research Intern' AND s.name='Legal Writing';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Clinical Research Intern' AND s.name='Clinical Research';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Clinical Research Intern' AND s.name='Research Methodology';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Pharmaceutical Research Intern' AND s.name='Lab Techniques';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Embedded Systems Intern' AND s.name='Embedded Systems';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Embedded Systems Intern' AND s.name='C++';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Mechanical Design Intern' AND s.name='SolidWorks';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Mechanical Design Intern' AND s.name='AutoCAD';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Structural Engineering Intern' AND s.name='Structural Analysis';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Structural Engineering Intern' AND s.name='AutoCAD';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Agricultural Research Intern' AND s.name='Agricultural Science';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Graphic Design Intern' AND s.name='Graphic Design';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Graphic Design Intern' AND s.name='Adobe Photoshop';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Hotel Management Intern' AND s.name='Hospitality Management';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Hotel Management Intern' AND s.name='Customer Service';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Supply Chain Intern' AND s.name='Supply Chain';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='Supply Chain Intern' AND s.name='Data Analysis';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='Environmental Research Intern' AND s.name='Environmental Assessment';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='EdTech Content Intern' AND s.name='Teaching';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='EdTech Content Intern' AND s.name='Content Writing';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 5 FROM internships i, skills s WHERE i.title='HR Operations Intern' AND s.name='Communication';
INSERT IGNORE INTO internship_skills (internship_id, skill_id, importance) SELECT i.id, s.id, 4 FROM internships i, skills s WHERE i.title='HR Operations Intern' AND s.name='Excel';

-- =====================================================================
--  INTEREST TAGS
-- =====================================================================
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'software' FROM internships WHERE title='Software Development Intern' AND company_name='National Informatics Centre';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'coding' FROM internships WHERE title='Software Development Intern' AND company_name='National Informatics Centre';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'web development' FROM internships WHERE title='Full Stack Web Developer Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'data science' FROM internships WHERE title='Data Science Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'ai' FROM internships WHERE title='Data Science Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'machine learning' FROM internships WHERE title='AI/ML Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'cybersecurity' FROM internships WHERE title='Cybersecurity Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'cloud' FROM internships WHERE title='Cloud Computing Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'mobile development' FROM internships WHERE title='Mobile App Development Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'analytics' FROM internships WHERE title='Data Analytics Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'data' FROM internships WHERE title='Data Analytics Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'finance' FROM internships WHERE title='Finance & Accounts Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'banking' FROM internships WHERE title='Banking Operations Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'investment' FROM internships WHERE title='Investment Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'fintech' FROM internships WHERE title='FinTech Product Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'marketing' FROM internships WHERE title='Digital Marketing Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'social media' FROM internships WHERE title='Digital Marketing Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'writing' FROM internships WHERE title='Content Writing Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'journalism' FROM internships WHERE title='Journalism Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'design' FROM internships WHERE title='UI/UX Design Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'graphic design' FROM internships WHERE title='Graphic Design Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'fashion' FROM internships WHERE title='Fashion Design Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'legal' FROM internships WHERE title='Legal Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'corporate law' FROM internships WHERE title='Corporate Law Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'healthcare' FROM internships WHERE title='Clinical Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'public health' FROM internships WHERE title='Public Health Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'pharmaceutical' FROM internships WHERE title='Pharmaceutical Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'embedded systems' FROM internships WHERE title='Embedded Systems Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'iot' FROM internships WHERE title='IoT Solutions Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'automotive' FROM internships WHERE title='Mechanical Design Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'construction' FROM internships WHERE title='Structural Engineering Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'robotics' FROM internships WHERE title='Robotics Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'space' FROM internships WHERE title='Physics Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'biotechnology' FROM internships WHERE title='Biotechnology Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'agriculture' FROM internships WHERE title='Agricultural Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'agritech' FROM internships WHERE title='AgriTech Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'environment' FROM internships WHERE title='Environmental Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'sustainability' FROM internships WHERE title='Sustainability Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'edtech' FROM internships WHERE title='EdTech Content Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'teaching' FROM internships WHERE title='Teacher Training Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'hospitality' FROM internships WHERE title='Hotel Management Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'tourism' FROM internships WHERE title='Tourism Planning Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'hr' FROM internships WHERE title='HR Operations Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'psychology' FROM internships WHERE title='Counseling Psychology Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'social work' FROM internships WHERE title='Social Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'ngo' FROM internships WHERE title='NGO Program Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'heritage' FROM internships WHERE title='Heritage Conservation Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'veterinary' FROM internships WHERE title='Veterinary Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'supply chain' FROM internships WHERE title='Supply Chain Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'quantitative' FROM internships WHERE title='Quantitative Research Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'architecture' FROM internships WHERE title='Architecture Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'publishing' FROM internships WHERE title='Editorial Intern';
INSERT IGNORE INTO internship_interest_tags (internship_id, tag) SELECT id, 'policy' FROM internships WHERE title='Policy Research Intern';
