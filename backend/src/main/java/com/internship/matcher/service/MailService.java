package com.internship.matcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
            // SMTP not configured yet - OTP is logged to console by OtpService instead.
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
}
