package com.internship.matcher.service;

import com.internship.matcher.dto.request.LoginRequest;
import com.internship.matcher.dto.request.RegisterRequest;
import com.internship.matcher.dto.response.AuthResponse;
import com.internship.matcher.exception.ApiException;
import com.internship.matcher.model.entity.Student;
import com.internship.matcher.model.enums.Enums.AuthProvider;
import com.internship.matcher.model.enums.Enums.OtpPurpose;
import com.internship.matcher.repository.StudentRepository;
import com.internship.matcher.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;

    /**
     * Step 1 of registration: create the (unverified) account and fire off an OTP.
     */
    @Transactional
    public void register(RegisterRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(HttpStatus.CONFLICT, "An account with this email already exists");
        }

        Student student = Student.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .authProvider(AuthProvider.LOCAL)
                .emailVerified(false)
                .profileComplete(false)
                .build();

        studentRepository.save(student);
        otpService.generateAndSendOtp(request.getEmail(), OtpPurpose.REGISTRATION);
    }

    /**
     * Step 2 of registration: verify the OTP the student received by email, mark
     * the account verified, and issue tokens so they're immediately logged in.
     */
    @Transactional
    public AuthResponse verifyRegistrationOtp(String email, String otpCode) {
        boolean valid = otpService.verifyOtp(email, otpCode, OtpPurpose.REGISTRATION);
        if (!valid) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Account not found"));

        student.setEmailVerified(true);
        studentRepository.save(student);

        return buildAuthResponse(student, "Email verified successfully. Welcome!");
    }

    /**
     * Standard email + password login. Requires the account to be email-verified.
     */
    public AuthResponse login(LoginRequest request) {
        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (student.getAuthProvider() == AuthProvider.GOOGLE && student.getPasswordHash() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "This account uses Google Sign-In. Please continue with Google.");
        }

        if (!passwordEncoder.matches(request.getPassword(), student.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        if (!student.isEmailVerified()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Please verify your email with the OTP sent to you before logging in");
        }

        return buildAuthResponse(student, "Login successful");
    }

    /**
     * Resends an OTP for registration / login / password reset flows.
     */
    public void resendOtp(String email, OtpPurpose purpose) {
        if (!studentRepository.existsByEmail(email) && purpose != OtpPurpose.REGISTRATION) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No account found with this email");
        }
        otpService.generateAndSendOtp(email, purpose);
    }

    @Transactional
    public AuthResponse resetPassword(String email, String otpCode, String newPassword) {
        boolean valid = otpService.verifyOtp(email, otpCode, OtpPurpose.PASSWORD_RESET);
        if (!valid) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Account not found"));
        student.setPasswordHash(passwordEncoder.encode(newPassword));
        studentRepository.save(student);
        return buildAuthResponse(student, "Password reset successful");
    }

    private AuthResponse buildAuthResponse(Student student, String message) {
        String token = jwtUtil.generateToken(student.getId(), student.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(student.getId(), student.getEmail());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .studentId(student.getId())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .profileComplete(student.isProfileComplete())
                .message(message)
                .build();
    }
}
