package com.internship.matcher.config;

import com.internship.matcher.model.entity.Student;
import com.internship.matcher.model.enums.Enums.AuthProvider;
import com.internship.matcher.repository.StudentRepository;
import com.internship.matcher.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Fires when a student successfully logs in with "Continue with Google".
 * Creates the student account on first login (no OTP needed - Google already
 * verified their email), issues our own JWT, then redirects back to the
 * frontend dashboard with the token attached so the SPA can store it.
 */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final StudentRepository studentRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");
        String picture = oAuth2User.getAttribute("picture");

        Student student = studentRepository.findByEmail(email).orElseGet(() -> {
            Student newStudent = Student.builder()
                    .fullName(name != null ? name : "Student")
                    .email(email)
                    .googleId(googleId)
                    .profilePictureUrl(picture)
                    .authProvider(AuthProvider.GOOGLE)
                    .emailVerified(true)
                    .profileComplete(false)
                    .build();
            return studentRepository.save(newStudent);
        });

        // Keep googleId / picture fresh even for returning users
        if (student.getGoogleId() == null) {
            student.setGoogleId(googleId);
            student.setAuthProvider(AuthProvider.GOOGLE);
            student.setEmailVerified(true);
            student.setProfilePictureUrl(picture);
            studentRepository.save(student);
        }

        String token = jwtUtil.generateToken(student.getId(), student.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(student.getId(), student.getEmail());

        String target = redirectUri
                + "?token=" + token
                + "&refreshToken=" + refreshToken
                + "&studentId=" + student.getId()
                + "&profileComplete=" + student.isProfileComplete();

        getRedirectStrategy().sendRedirect(request, response, target);
    }
}
