package com.round.airplaneticketbooking.customer.auth;

import com.round.airplaneticketbooking.customer.Customer;
import com.round.airplaneticketbooking.customer.CustomerRepository;
import com.round.airplaneticketbooking.enumsAndTemplates.AuthenticationToken;
import com.round.airplaneticketbooking.enumsAndTemplates.RegisterRequest;
import com.round.airplaneticketbooking.exception.CustomAuthenticationException;
import com.round.airplaneticketbooking.util.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserAuthenticationService(CustomerRepository customerRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtTokenProvider jwtTokenProvider) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthenticationToken register(RegisterRequest request) {
        Customer customer = Customer.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();

        customerRepository.save(customer);

        String jwtToken = jwtTokenProvider.generateToken(customer.getCustomerId());

        return AuthenticationToken
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationToken authenticate(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomAuthenticationException("Invalid credentials."));

        if (!passwordEncoder.matches(password, customer.getPassword())) {
            throw new CustomAuthenticationException("Invalid credentials.");
        }

        String token = jwtTokenProvider.generateToken(customer.getCustomerId());

        return AuthenticationToken
                .builder()
                .token(token)
                .build();
    }

}
