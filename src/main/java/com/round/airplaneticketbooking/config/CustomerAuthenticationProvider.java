package com.round.airplaneticketbooking.config;

import com.round.airplaneticketbooking.model.Customer;
import com.round.airplaneticketbooking.repository.CustomerRepository;
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
public class CustomerAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Customer customer = customerRepository
                .findByEmail(email)
                .orElse(null);

        if (customer != null && passwordEncoder.matches(password, customer.getPassword())) {
            return new UsernamePasswordAuthenticationToken(customer, password, customer.getAuthorities());
        } else {
            throw new AuthenticationServiceException("Invalid email or password.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
