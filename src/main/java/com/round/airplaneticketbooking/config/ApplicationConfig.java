package com.round.airplaneticketbooking.config;

import com.round.airplaneticketbooking.repository.AdminRepository;
import com.round.airplaneticketbooking.repository.CustomerRepository;
import com.round.airplaneticketbooking.util.AdminDetailsService;
import com.round.airplaneticketbooking.util.CustomerDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new AdminAuthenticationProvider(adminRepository, passwordEncoder()))
                .authenticationProvider(new CustomerAuthenticationProvider(customerRepository, passwordEncoder()));
        return authenticationManagerBuilder.build();
    }
}
