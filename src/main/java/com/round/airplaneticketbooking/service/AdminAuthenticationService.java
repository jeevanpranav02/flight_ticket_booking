package com.round.airplaneticketbooking.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.round.airplaneticketbooking.constants.enums.Role;
import com.round.airplaneticketbooking.constants.request.RegisterRequest;
import com.round.airplaneticketbooking.constants.response.AuthenticationToken;
import com.round.airplaneticketbooking.exception.CustomAuthenticationException;
import com.round.airplaneticketbooking.model.Admin;
import com.round.airplaneticketbooking.repository.AdminRepository;
import com.round.airplaneticketbooking.util.JwtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAuthenticationService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationToken register(RegisterRequest request) {
        Admin admin = Admin.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .email(request.getEmail())
                .build();

        adminRepository.save(admin);

        String jwtToken = tokenProvider.generateToken(admin);

        return AuthenticationToken
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationToken authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found : " + email));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new CustomAuthenticationException("Invalid credentials.");
        }

        // Generate an authentication token
        String token = tokenProvider.generateToken(admin);

        return AuthenticationToken
                .builder()
                .token(token)
                .build();
    }
}
