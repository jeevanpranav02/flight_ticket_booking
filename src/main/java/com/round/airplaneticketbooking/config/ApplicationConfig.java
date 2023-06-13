//package com.round.airplaneticketbooking.config;
//
//import com.round.airplaneticketbooking.customer.CustomerRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//@Configuration
//@RequiredArgsConstructor
//public class ApplicationConfig {
//    @Value("${app.jwt.secret}")
//    private String jwtSecret;
//    @Value("${app.jwt.expiration}")
//    private int jwtExpiration;
//
//    private final CustomerRepository customerRepository;
//
//    @Bean
//    public UserDetailsService customerDetailsService() {
//        return username -> customerRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
//    }
//
//    @Bean
//    public AuthenticationProvider customerAuthenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(customerDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
