package com.round.airplaneticketbooking.config;

import com.round.airplaneticketbooking.model.Admin;
import com.round.airplaneticketbooking.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAuthenticationProvider implements AuthenticationProvider {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Admin admin = adminRepository
                .findByEmail(email)
                .orElse(null);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return new UsernamePasswordAuthenticationToken(admin, password, admin.getAuthorities());
        } else {
            throw new AuthenticationServiceException("Invalid email or password.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
