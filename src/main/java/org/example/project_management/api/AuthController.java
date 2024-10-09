package org.example.project_management.api;

import jakarta.validation.Valid;
import org.example.project_management.dto.auth.AuthRequest;
import org.example.project_management.dto.auth.AuthResponse;
import org.example.project_management.dto.auth.RefreshTokenResponse;
import org.example.project_management.security.CustomUserDetailsService;
import org.example.project_management.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername());
            String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to generate token", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to generate token: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        try {
            // Validate the refresh token and generate a new access token
            String username = jwtUtils.extractUsername(refreshToken);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtils.validateToken(refreshToken, userDetails.getUsername())) {
                String newAccessToken = jwtUtils.generateAccessToken(userDetails.getUsername()); // Generate new token
                return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } catch (Exception ex) {
            logger.error("Failed to refresh token", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to refresh token");
        }
    }
}
