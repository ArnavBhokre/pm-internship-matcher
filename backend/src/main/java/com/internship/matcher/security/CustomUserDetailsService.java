package com.internship.matcher.security;

import com.internship.matcher.model.entity.Student;
import com.internship.matcher.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No account found for " + email));

        return User.builder()
                .username(student.getEmail())
                .password(student.getPasswordHash() == null ? "" : student.getPasswordHash())
                .authorities(new SimpleGrantedAuthority("ROLE_" + student.getRole().name()))
                .build();
    }
}
