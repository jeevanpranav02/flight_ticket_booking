package com.round.airplaneticketbooking.util;

import com.round.airplaneticketbooking.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String jwtSecret;

    private final int jwtExpiration;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtSecret = jwtConfig.getJwtSecret();
        this.jwtExpiration = jwtConfig.getJwtExpiration();
    }


    public String generateToken(Long userID) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts
                .builder()
                .setSubject(Long.toString(userID))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getIssuer());
    }

    public boolean validateToken(String token, Long userId) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Check if the subject (user ID) in the token matches the provided user ID
            String tokenUserId = claims.getSubject();
            return tokenUserId.equals(userId.toString());
        } catch (Exception ex) {
            // Token is invalid or expired
            return false;
        }
    }


    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
