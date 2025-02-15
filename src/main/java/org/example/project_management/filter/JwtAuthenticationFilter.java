package org.example.project_management.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.project_management.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for JWT authentication.
 * This filter intercepts requests to validate the JWT token and authenticate the user.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for JwtAuthenticationFilter.
     *
     * @param jwtUtil the JWT utility class
     * @param userDetailsService the user details service
     */
    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests to validate JWT tokens.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param chain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip JWT filtering for Swagger UI and OpenAPI documentation paths
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.equals("/swagger-ui.html")
        ) {
            chain.doFilter(request, response);
            return;
        }

        // Extract tokens from headers
        final String authorizationHeader = request.getHeader("Authorization");

        // If no Authorization header or doesn't start with "Bearer", skip authentication
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7); // Extract JWT token after "Bearer "
        String username = null;

        try {
            // Attempt to extract username from the token
            username = jwtUtil.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT Token is expired. Please refresh your token: " + e.getMessage());
            return;
            // Let Spring Security handle this exception by throwing it
            //throw new TokenExpiredException("JWT token expired");
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT token", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
            // Let Spring Security handle this exception by throwing it
            //throw new IllegalArgumentException("Invalid JWT token");
        } catch (Exception e) {
            logger.error("Invalid JWT token", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token error");
            return;
            // Let Spring Security handle this exception by throwing it
            //throw new IllegalArgumentException("JWT token error");
        }

        // If username is found, check for authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token and authenticate if valid
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
