package com.round.airplaneticketbooking.admin.auth;

import com.round.airplaneticketbooking.admin.Admin;
import com.round.airplaneticketbooking.admin.AdminRepository;
import com.round.airplaneticketbooking.enumsAndTemplates.AuthenticationToken;
import com.round.airplaneticketbooking.exception.CustomAuthenticationException;
import com.round.airplaneticketbooking.util.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthenticationService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AdminAuthenticationService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthenticationToken authenticate(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new CustomAuthenticationException("Invalid credentials."));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new CustomAuthenticationException("Invalid credentials.");
        }

        // Generate an authentication token
        String token = generateAuthToken(admin);

        return new AuthenticationToken(token);
    }

    private String generateAuthToken(Admin admin) {
        Long adminId = admin.getAdminId();

        String token = tokenProvider.generateToken(adminId);

        return token;
    }
}
