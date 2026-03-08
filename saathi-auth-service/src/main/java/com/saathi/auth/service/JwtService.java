package com.saathi.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility service for creating and validating JWT tokens used by the security
 * filter and authentication flows.
 */
public interface JwtService {
    /**
     * Generate a JWT token containing claims for the provided user.
     *
     * @param userDetails user information
     * @return signed JWT token as String
     */
    String generateToken(UserDetails userDetails);

    /**
     * Extract username (subject) from the token.
     *
     * @param token JWT token
     * @return username contained in token
     */
    String extractUsername(String token);

    /**
     * Validate the token is valid and belongs to the provided user.
     *
     * @param token       JWT token
     * @param userDetails user details to validate against
     * @return true if token is valid and not expired
     */
    boolean validateToken(String token);

    /**
     * Return the configured token expiration time in milliseconds.
     */
    long getExpirationTime();

    /**
     * Extract all claims from the token.
     *
     * @param token JWT token
     * @return Claims object containing all token claims
     */
    Claims extractAllClaims(String token);
}
