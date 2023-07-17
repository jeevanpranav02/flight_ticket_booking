
package com.round.airplaneticketbooking.config.security;

import static com.round.airplaneticketbooking.constants.enums.Role.ADMIN;
import static com.round.airplaneticketbooking.constants.enums.Role.CUSTOMER;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.round.airplaneticketbooking.config.AdminAuthenticationProvider;
import com.round.airplaneticketbooking.config.CustomerAuthenticationProvider;
import com.round.airplaneticketbooking.config.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AdminAuthenticationProvider adminAuthenticationProvider;
    private final CustomerAuthenticationProvider customerAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/admin/**")
                        .hasAuthority(ADMIN.name()).requestMatchers("/api/v1/customer/**")
                        .hasAuthority(CUSTOMER.name()).requestMatchers("/auth/v1/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(adminAuthenticationProvider)
                .authenticationProvider(customerAuthenticationProvider).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
