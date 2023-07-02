package com.round.airplaneticketbooking.config;

import com.round.airplaneticketbooking.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService adminDetailsService;
    private final UserDetailsService customerDetailsService;

    public JWTAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   UserDetailsService adminDetailsService,
                                   UserDetailsService customerDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminDetailsService = adminDetailsService;
        this.customerDetailsService = customerDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwtToken;
        Long userId = null;

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // If the Authorization header is missing or does not start with "Bearer ", proceed to the next filter
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authorizationHeader.substring(7);
        String userType = getUserTypeFromRequest(request);

        if (userType != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            UserDetailsService userDetailsService;

            if (userType.equals("admin")) {
                userId = jwtTokenProvider.getUserIdFromToken(jwtToken);
                userDetails = adminDetailsService.loadUserByUsername(userId.toString());
                userDetailsService = adminDetailsService;
            } else if (userType.equals("customer")) {
                userId = jwtTokenProvider.getUserIdFromToken(jwtToken);
                userDetails = customerDetailsService.loadUserByUsername(userId.toString());
                userDetailsService = customerDetailsService;
            } else {
                // Unknown user type
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtTokenProvider.validateToken(jwtToken, userId)) {
                // If the JWT token is valid, create an authentication token and set it in the security context
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // Invalid token
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getUserTypeFromRequest(HttpServletRequest request) {
        // Retrieve the user type from the request URL or headers
        // You can modify this code according to your application's URL structure or header conventions
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/admin/")) {
            return "admin";
        } else if (requestURI.contains("/customer/")) {
            return "customer";
        }
        return null;
    }
}
