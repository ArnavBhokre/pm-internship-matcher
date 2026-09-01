package com.internship.matcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the PM Internship Scheme - AI Recommendation Engine backend.
 *
 * Run with: mvn spring-boot:run
 * or:       java -jar target/pm-internship-matcher-1.0.0.jar
 *
 * Make sure MySQL is running locally and a database named "internship" exists
 * (schema will auto-create/update tables on startup via Hibernate ddl-auto=update,
 * and database/internship_schema.sql is provided for manual setup / reference).
 */
@SpringBootApplication
public class MatcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatcherApplication.class, args);
    }
}
