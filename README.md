# PM Internship Matcher — AI-Based Internship Recommendation Engine

A full-stack web application that matches students with suitable internships under
the **PM Internship Scheme**, based on their skills, qualifications, interests,
preferred locations and career goals.

- **Backend:** Java 17, Spring Boot 3, Spring Security, Spring Data JPA, MySQL, JWT, Google OAuth2
- **Frontend:** HTML / CSS / vanilla JavaScript (no build step — just open in Chrome)
- **Database:** MySQL, database name `internship`
- **AI matching:** an explainable, weighted multi-factor scoring engine (see below)

---

## 1. What's included

```
pm-internship-matcher/
├── backend/                 Spring Boot project (Maven)
│   ├── pom.xml
│   └── src/main/java/com/internship/matcher/
│       ├── config/          Security, CORS, Google OAuth2 success handler
│       ├── controller/      REST controllers
│       ├── service/         Business logic incl. the recommendation engine
│       ├── repository/      Spring Data JPA repositories
│       ├── model/entity/    JPA entities (Student, Internship, Skill, Application...)
│       ├── security/        JWT filter + user details service
│       ├── dto/             Request/response DTOs
│       └── util/            JWT utility
│   └── src/main/resources/application.properties
├── frontend/                 Static site — open directly in Chrome
│   ├── index.html            Login (+ "Continue with Google")
│   ├── register.html         Sign up
│   ├── verify-otp.html       OTP verification
│   ├── forgot-password.html
│   ├── dashboard.html        Overview + quick actions
│   ├── profile.html          Skills / qualifications / interests / locations
│   ├── recommendations.html  AI-ranked personalized matches
│   ├── browse.html           Search/filter all internships
│   ├── applications.html     Application status tracker
│   ├── css/style.css
│   └── js/                   config.js, api.js, nav.js, profile.js, browse.js
└── database/
    ├── internship_schema.sql  Manual/reference schema
    └── seed_data.sql          Sample skills + 8 demo internships
```

---

## 2. Where to put this on your machine

Extract this zip so that you end up with:

```
D:\arnav\project\pm-internship-matcher\
```

(i.e. unzip directly into `D:\arnav\project\`).

---

## 3. Prerequisites

Install these once, if you don't already have them:

| Tool | Version | Check with |
|---|---|---|
| Java JDK | 17+ | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| MySQL Server | 8.x | `mysql --version` |
| Google Chrome | any recent | — |

> **About "without internet":** the backend and MySQL run entirely on `localhost` — no
> internet connection is required to use the app once it's set up. The *first*
> `mvn spring-boot:run` **does** need internet once, to download the Maven
> dependencies listed in `pom.xml` (they're cached afterwards). Real OTP email
> delivery and "Continue with Google" also need internet, since they talk to
> Gmail's SMTP servers and Google's OAuth servers respectively — see the OTP
> note in step 6 for a way to test the OTP flow completely offline.

---

## 4. Set up MySQL

Open a terminal (or MySQL Workbench) and create the database:

```sql
CREATE DATABASE internship;
```

That's it — Spring Boot (Hibernate `ddl-auto=update`) will automatically create every
table the first time you run the backend. `database/internship_schema.sql` is
provided only for reference or if you'd rather create the tables manually.

To load sample internships so you have data to test with:

```bash
mysql -u root -p internship < database/seed_data.sql
```
(Run this **after** starting the backend once, so the tables already exist.)

---

## 5. Configure the backend

Open `backend/src/main/resources/application.properties` and fill in:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

That's the **only required change** to run the app locally with console-logged OTPs.

Optional (only needed for real email / real Google login — see sections 6 & 7):

```properties
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
app.mail.enabled=true

spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
```

---

## 6. Run the backend

```bash
cd D:\arnav\project\pm-internship-matcher\backend
mvn spring-boot:run
```

The API starts on **http://localhost:8080**.

### Testing OTP without setting up email (recommended for local dev)
`app.otp.log-to-console=true` is on by default, so every OTP generated is printed
straight to the terminal running the backend, e.g.:

```
========== OTP for student@example.com (REGISTRATION) => 483920  [valid 5 min] ==========
```

Just copy that code into the "Verify OTP" screen in the browser. No SMTP setup
needed at all to fully test registration/login.

### Enabling real OTP emails
1. Turn on 2-Step Verification on your Gmail account.
2. Create an **App Password**: Google Account → Security → App passwords.
3. In `application.properties`, set `spring.mail.username`, `spring.mail.password`
   (the app password, not your normal Gmail password) and `app.mail.enabled=true`.

---

## 7. Enabling "Continue with Google" (optional)

1. Go to [Google Cloud Console → Credentials](https://console.cloud.google.com/apis/credentials).
2. Create an **OAuth 2.0 Client ID** (Application type: Web application).
3. Add this **Authorized redirect URI**:
   ```
   http://localhost:8080/login/oauth2/code/google
   ```
4. Copy the generated **Client ID** and **Client Secret** into
   `application.properties`:
   ```properties
   spring.security.oauth2.client.registration.google.client-id=...
   spring.security.oauth2.client.registration.google.client-secret=...
   ```
5. Restart the backend. The "Continue with Google" buttons on the login/register
   pages will now work — clicking them sends the browser to Google's consent
   screen, and on success the backend creates/loads the student account, issues
   a JWT, and redirects back to `dashboard.html` (or `profile.html` if their
   profile isn't complete yet) logged in automatically.

Until you configure this, the Google buttons will show Google's "invalid client"
page — everything else in the app works fine without it, using email/password + OTP.

---

## 8. Run the frontend (in Chrome)

No build step, no npm install — it's plain HTML/CSS/JS.

**Easiest way:** in Chrome, press `Ctrl+O` and open:
```
D:\arnav\project\pm-internship-matcher\frontend\index.html
```

**Recommended way** (avoids some browsers' quirks with `file://` pages calling
APIs): serve the folder locally instead of opening the file directly.
If you have Python installed:
```bash
cd D:\arnav\project\pm-internship-matcher\frontend
python -m http.server 5500
```
then open **http://localhost:5500** in Chrome. (VS Code's "Live Server"
extension also works great here.)

> The backend's CORS config already allows both `file://` pages (`null` origin)
> and `http://localhost:5500`, so either approach works out of the box.

---

## 9. Using the app

1. **Register** with your name, email, phone, and a password → you'll receive an OTP.
2. **Verify the OTP** (check the backend console if you haven't set up SMTP).
3. You're logged in → you'll land on **My Profile**. Fill in:
   - Qualification, field of study, college, graduation year, CGPA
   - Skills (with proficiency: Beginner/Intermediate/Advanced/Expert)
   - Interests (chips, e.g. "data", "marketing", "design")
   - Career goals (free text)
   - Preferred locations (chips) and work mode
4. Save → you're redirected to **Recommendations**: internships ranked by an
   overall match score (0–100%), with a breakdown of skill/interest/location/
   qualification sub-scores, matched vs. missing skills, and a plain-English
   explanation of the match.
5. **Apply** directly from a recommendation card, or browse everything via
   **Browse Internships** with keyword/location/sector filters.
6. Track status under **My Applications**.

---

## 10. How the AI recommendation engine works

Implemented in `backend/.../service/RecommendationEngine.java`. It's a
transparent, explainable **weighted multi-factor content-based scorer** — no
training data required, and every score comes with a human-readable reason,
which matters a lot for a student-facing tool:

```
overallScore = 0.40 × skillMatch
             + 0.20 × interestMatch
             + 0.20 × locationMatch
             + 0.15 × qualificationMatch
             + 0.05 × careerGoalKeywordMatch
```

- **Skill match** — importance-weighted overlap between the internship's
  required skills and the student's skills, boosted by proficiency level
  (Beginner → Expert).
- **Interest match** — overlap between the student's stated interests and the
  internship's sector + interest tags (with fuzzy/partial matching).
- **Location match** — exact/partial match against preferred locations; remote
  roles always score 100%.
- **Qualification match** — checks the student's qualification against the
  internship's minimum qualification requirement (handles multi-qualification
  strings like `"B.Tech/B.E./MCA"`).
- **Career goal match** — lightweight keyword overlap between the student's
  free-text career goals and the internship's title/description/sector.

This design is intentionally modular: `RecommendationEngine.generateRecommendations()`
is the single entry point the rest of the app talks to, so you could later swap
in a learned model (embeddings/collaborative filtering) without touching any
controller or frontend code.

---

## 11. Key features checklist

- ✅ Student registration with **email + OTP verification**
- ✅ **Google Sign-In** ("Continue with Google") — no password needed
- ✅ JWT-based authenticated sessions
- ✅ Student profile creation: qualifications, skills (with proficiency),
  interests, career goals, preferred locations & work mode
- ✅ AI-based skill & qualification analysis
- ✅ Interest and career-preference matching
- ✅ Location-based recommendations (with remote-role handling)
- ✅ Personalized, ranked internship suggestions with a transparent
  suitability score and explanation
- ✅ Easy internship discovery: search/filter by keyword, location, sector
- ✅ Apply to internships + application status tracking
- ✅ MySQL persistence, seed data for demoing immediately

---

## 12. Troubleshooting

- **"Could not reach the server"** in the browser → make sure `mvn spring-boot:run`
  is still running and shows `Started MatcherApplication` with no errors.
- **MySQL connection errors** → confirm MySQL is running, the `internship`
  database exists, and the username/password in `application.properties` are correct.
- **Login says "please verify your email"** → you registered but haven't entered
  the OTP yet; go to `verify-otp.html` (or register again — a fresh OTP is sent
  each time).
- **Google button doesn't work** → you haven't added your own Google OAuth
  Client ID/Secret yet (see section 7). Everything else in the app works without it.
- **Port 8080 already in use** → change `server.port` in `application.properties`
  and update `API_BASE_URL` in `frontend/js/config.js` to match.

---

## 13. Building a runnable JAR (optional)

```bash
cd backend
mvn clean package
java -jar target/pm-internship-matcher-1.0.0.jar
```
