package com.round.airplaneticketbooking.config;

import com.round.airplaneticketbooking.util.AdminDetailsService;
import com.round.airplaneticketbooking.util.CustomerDetailsService;
import com.round.airplaneticketbooking.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AdminDetailsService adminDetailsService;
    private final CustomerDetailsService customerDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwtToken;
        String userEmail = null;

        // If the Authorization header is missing or does not start with "Bearer ", proceed to the next filter
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authorizationHeader.substring(7);
        userEmail = jwtService.extractUsername(jwtToken);
        String userType = getUserTypeFromRequest(request);

        if (userType != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;

            if (userType.equals("admin")) {
                userDetails = adminDetailsService.loadUserByUsername(userEmail);
            } else if (userType.equals("customer")) {
                userDetails = customerDetailsService.loadUserByUsername(userEmail);
            } else {
                // Unknown user type
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                // If the JWT token is valid, create an authentication token and set it in the security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getUserTypeFromRequest(HttpServletRequest request) {
        // Retrieve the user type from the request URL or headers
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/admin/")) {
            return "admin";
        } else if (requestURI.contains("/customer/")) {
            return "customer";
        }
        return null;
    }
}
