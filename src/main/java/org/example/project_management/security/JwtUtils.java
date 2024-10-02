package org.example.project_management.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for JWT operations: generating, parsing, and validating JWT tokens
 */
@Component
public class JwtUtils {
    @Value("${org.example.project_management.jwt.secret}")
    private String secret;
    @Value("${org.example.project_management.jwt.expiration}")
    private String expirationTimeMs;

    // Generate a JWT token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + Integer.parseInt(expirationTimeMs)))
                .signWith(SignatureAlgorithm.HS384, secret)
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    // Validate token
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
