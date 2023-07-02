package com.round.airplaneticketbooking.service;

import com.round.airplaneticketbooking.model.Customer;
import com.round.airplaneticketbooking.repository.CustomerRepository;
import com.round.airplaneticketbooking.constants.response.AuthenticationToken;
import com.round.airplaneticketbooking.constants.request.RegisterRequest;
import com.round.airplaneticketbooking.constants.enums.Role;
import com.round.airplaneticketbooking.exception.CustomAuthenticationException;
import com.round.airplaneticketbooking.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerAuthenticationService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationToken register(RegisterRequest request) {
        Customer customer = Customer.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.CUSTOMER)
                .email(request.getEmail())
                .build();

        customerRepository.save(customer);

        String jwtToken = jwtService.generateToken(customer);

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
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found : " + email));

        if (!passwordEncoder.matches(password, customer.getPassword())) {
            throw new CustomAuthenticationException("Invalid credentials.");
        }

        String token = jwtService.generateToken(customer);

        return AuthenticationToken
                .builder()
                .token(token)
                .build();
    }
}
