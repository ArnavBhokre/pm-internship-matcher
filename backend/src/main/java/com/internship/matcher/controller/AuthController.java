package com.internship.matcher.controller;

import com.internship.matcher.dto.request.LoginRequest;
import com.internship.matcher.dto.request.OtpSendRequest;
import com.internship.matcher.dto.request.OtpVerifyRequest;
import com.internship.matcher.dto.request.RegisterRequest;
import com.internship.matcher.dto.response.ApiResponse;
import com.internship.matcher.dto.response.AuthResponse;
import com.internship.matcher.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** Step 1: create account + send OTP to email */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("Registration successful. An OTP has been sent to your email."));
    }

    /** Step 2: verify the OTP sent during registration -> returns JWT, logs the user in */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        AuthResponse response = authService.verifyRegistrationOtp(request.getEmail(), request.getOtpCode());
        return ResponseEntity.ok(ApiResponse.ok(response.getMessage(), response));
    }

    /** Resend an OTP (registration / login / password reset) */
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@Valid @RequestBody OtpSendRequest request) {
        authService.resendOtp(request.getEmail(), request.getPurpose());
        return ResponseEntity.ok(ApiResponse.ok("OTP resent successfully"));
    }

    /** Standard email + password login (account must already be OTP-verified) */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    /** Step 1 of "forgot password": send OTP */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody OtpSendRequest request) {
        authService.resendOtp(request.getEmail(), request.getPurpose());
        return ResponseEntity.ok(ApiResponse.ok("Password reset OTP sent to your email"));
    }

    /** Step 2 of "forgot password": verify OTP + set new password */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String email,
                                                       @RequestParam String otpCode,
                                                       @RequestParam String newPassword) {
        AuthResponse response = authService.resetPassword(email, otpCode, newPassword);
        return ResponseEntity.ok(ApiResponse.ok(response.getMessage(), response));
    }

    /**
     * "Continue with Google" entry point - the frontend simply navigates the
     * browser to GET /api/auth/google, Spring Security handles the OAuth2
     * redirect dance with Google, and OAuth2LoginSuccessHandler sends the
     * user back to the frontend with a JWT once they approve.
     */
    @GetMapping("/google")
    public void googleLoginInfo() {
        // Actual redirect is handled by Spring Security's /oauth2/authorization/google
        // The frontend links directly to that URL - this endpoint exists just for discoverability.
    }
}
