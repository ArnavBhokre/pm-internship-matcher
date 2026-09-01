package com.internship.matcher.service;

import com.internship.matcher.model.entity.OtpVerification;
import com.internship.matcher.model.enums.Enums.OtpPurpose;
import com.internship.matcher.repository.OtpVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpVerificationRepository otpRepository;
    private final MailService mailService;

    @Value("${app.otp.expiry-minutes}")
    private int expiryMinutes;

    @Value("${app.otp.length}")
    private int otpLength;

    @Value("${app.otp.log-to-console}")
    private boolean logToConsole;

    private static final SecureRandom RANDOM = new SecureRandom();

    public void generateAndSendOtp(String email, OtpPurpose purpose) {
        String otpCode = generateNumericOtp(otpLength);

        OtpVerification otp = OtpVerification.builder()
                .email(email)
                .otpCode(otpCode)
                .purpose(purpose)
                .expiresAt(LocalDateTime.now().plusMinutes(expiryMinutes))
                .verified(false)
                .attemptCount(0)
                .build();

        otpRepository.save(otp);

        if (logToConsole) {
            log.info("========== OTP for {} ({}) => {}  [valid {} min] ==========",
                    email, purpose, otpCode, expiryMinutes);
        }

        mailService.sendOtpEmail(email, otpCode, expiryMinutes);
    }

    public boolean verifyOtp(String email, String otpCode, OtpPurpose purpose) {
        Optional<OtpVerification> latest = otpRepository.findTopByEmailAndPurposeOrderByCreatedAtDesc(email, purpose);

        if (latest.isEmpty()) {
            return false;
        }

        OtpVerification otp = latest.get();

        if (otp.isVerified()) {
            return false; // already used
        }
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false; // expired
        }
        otp.setAttemptCount(otp.getAttemptCount() + 1);
        if (otp.getAttemptCount() > 5) {
            otpRepository.save(otp);
            return false; // too many attempts
        }

        boolean matches = otp.getOtpCode().equals(otpCode);
        if (matches) {
            otp.setVerified(true);
        }
        otpRepository.save(otp);
        return matches;
    }

    private String generateNumericOtp(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
