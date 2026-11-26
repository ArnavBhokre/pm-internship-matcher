package com.internship.matcher.service;

import com.internship.matcher.model.entity.Application;
import com.internship.matcher.model.entity.Internship;
import com.internship.matcher.model.entity.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.mail.enabled}")
    private boolean mailEnabled;

    public void sendOtpEmail(String toEmail, String otpCode, int expiryMinutes) {
        if (!mailEnabled) {
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toEmail);
            message.setSubject("Your PM Internship Matcher verification code");
            message.setText("Your OTP is: " + otpCode + "\n\nThis code expires in "
                    + expiryMinutes + " minutes. Do not share it with anyone.");
            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send OTP email to {}: {}", toEmail, ex.getMessage());
        }
    }

    /**
     * Sends a confirmation email when a student applies for an internship.
     */
    @Async
    public void sendApplicationConfirmationEmail(Student student, Internship internship, Double matchScore) {
        if (!mailEnabled) {
            log.info("Mail disabled — skipping application confirmation for {} -> {}",
                    student.getEmail(), internship.getTitle());
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(student.getEmail());
            helper.setSubject("✅ Application Confirmed – " + internship.getTitle());

            String htmlContent = buildApplicationConfirmationHtml(student, internship, matchScore);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Application confirmation email sent to {} for internship: {}",
                    student.getEmail(), internship.getTitle());
        } catch (Exception ex) {
            log.error("Failed to send application confirmation to {}: {}",
                    student.getEmail(), ex.getMessage());
        }
    }

    private String buildApplicationConfirmationHtml(Student student, Internship internship, Double matchScore) {
        String scoreSection = "";
        if (matchScore != null) {
            String scoreColor = matchScore >= 75 ? "#128a45" : matchScore >= 50 ? "#ff7a1a" : "#d93025";
            scoreSection = String.format("""
                <div style="background: %s; color: white; display: inline-block; padding: 8px 18px; border-radius: 20px; font-size: 18px; font-weight: bold; margin: 12px 0;">
                    AI Match Score: %.0f%%
                </div>
                """, scoreColor, matchScore);
        }

        String stipend = internship.getStipendPerMonth() != null
                ? String.format("₹%,.0f/month", internship.getStipendPerMonth())
                : "Not specified";

        String deadline = internship.getApplicationDeadline() != null
                ? internship.getApplicationDeadline().toString()
                : "Not specified";

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"></head>
            <body style="font-family: 'Segoe UI', Arial, sans-serif; margin: 0; padding: 0; background: #f4f6f8;">
                <div style="max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">

                    <!-- Header -->
                    <div style="background: linear-gradient(135deg, #1a73e8, #0d47a1); padding: 32px 24px; text-align: center;">
                        <h1 style="color: white; margin: 0; font-size: 24px;">🎉 Application Submitted!</h1>
                        <p style="color: rgba(255,255,255,0.9); margin: 8px 0 0;">PM Internship Matcher</p>
                    </div>

                    <!-- Body -->
                    <div style="padding: 28px 24px;">
                        <p style="font-size: 16px; color: #333;">Dear <strong>%s</strong>,</p>

                        <p style="font-size: 15px; color: #444; line-height: 1.6;">
                            Congratulations! Your application has been successfully submitted for the following internship:
                        </p>

                        <!-- Internship Card -->
                        <div style="background: #f8f9fb; border: 1px solid #e0e4ea; border-radius: 10px; padding: 20px; margin: 20px 0;">
                            <h2 style="margin: 0 0 8px; color: #1a73e8; font-size: 20px;">%s</h2>
                            <p style="margin: 4px 0; color: #555; font-size: 14px;">🏢 <strong>%s</strong> &middot; %s</p>
                            <p style="margin: 4px 0; color: #555; font-size: 14px;">📍 %s &middot; %s</p>
                            <p style="margin: 4px 0; color: #555; font-size: 14px;">⏱ Duration: %s</p>
                            <p style="margin: 4px 0; color: #555; font-size: 14px;">💰 Stipend: %s</p>
                            <p style="margin: 4px 0; color: #555; font-size: 14px;">📅 Deadline: %s</p>
                        </div>

                        <!-- Match Score -->
                        <div style="text-align: center;">
                            %s
                        </div>

                        <!-- Next Steps -->
                        <div style="background: #e8f5e9; border-radius: 8px; padding: 16px; margin: 20px 0;">
                            <h3 style="margin: 0 0 10px; color: #2e7d32; font-size: 16px;">📋 What happens next?</h3>
                            <ul style="margin: 0; padding-left: 20px; color: #444; font-size: 14px; line-height: 1.8;">
                                <li>Your application is under review by the organization</li>
                                <li>Shortlisted candidates will be notified via email</li>
                                <li>Keep your profile updated for better matches</li>
                                <li>You can track your application status on the dashboard</li>
                            </ul>
                        </div>

                        <p style="font-size: 14px; color: #666; line-height: 1.6;">
                            You can view and manage all your applications by visiting your
                            <strong>Applications</strong> page on PM Internship Matcher.
                        </p>
                    </div>

                    <!-- Footer -->
                    <div style="background: #f8f9fb; padding: 20px 24px; text-align: center; border-top: 1px solid #e0e4ea;">
                        <p style="margin: 0; font-size: 13px; color: #888;">
                            This is an automated email from PM Internship Matcher.<br>
                            Please do not reply to this email.
                        </p>
                    </div>

                </div>
            </body>
            </html>
            """,
                student.getFullName(),
                internship.getTitle(),
                internship.getCompanyName(),
                internship.getSector() != null ? internship.getSector() : "General",
                internship.getLocation() != null ? internship.getLocation() : "Not specified",
                internship.getWorkMode() != null ? internship.getWorkMode().name() : "ONSITE",
                internship.getDuration() != null ? internship.getDuration() : "Not specified",
                stipend,
                deadline,
                scoreSection
        );
    }
}
