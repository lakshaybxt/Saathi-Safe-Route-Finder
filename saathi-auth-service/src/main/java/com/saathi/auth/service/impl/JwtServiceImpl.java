package com.saathi.auth.service.impl;


import com.saathi.auth.security.AuthUserDetails;
import com.saathi.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementation of JwtService for generating and validating JWT tokens.
 */
@Service
public class JwtServiceImpl implements JwtService {

  @Value("${security.jwt.secret}")
  private String secretKey;

  @Value("${security.jwt.expiration}")
  private long jwtExpiration;

  /**
   * Generate a JWT token containing claims for the provided user.
   *
   * @param userDetails user information
   * @return signed JWT token as String
   */
  @Override
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    AuthUserDetails testingUser = (AuthUserDetails) userDetails;
    claims.put("userId", testingUser.getId());
    claims.put("username", testingUser.user().getUsername());
    claims.put("email", testingUser.getUsername());
    claims.put("enabled", testingUser.isEnabled());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSigninKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Get signing key from the configured secret.
   */
  private SecretKey getSigninKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Extract username (subject) from the token.
   *
   * @param token JWT token
   * @return username contained in token
   */
  @Override
  public String extractUsername(String token) {
    return extractClaims(token, Claims::getSubject);
  }

  /**
   * Extract specific claims using the provided claims resolver function.
   */
  private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
    Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Extract all claims from the token.
   */
  public Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigninKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Validate the token is valid and belongs to the provided user.
   *
   * @param token JWT token
   * @return true if token is valid and not expired
   */
  @Override
  public boolean validateToken(String token) {
    return !isTokenExpired(token);
  }

  /**
   * Check if the token is expired.
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extract expiration date from the token.
   */
  private Date extractExpiration(String token) {
    return extractClaims(token, Claims::getExpiration);
  }

  /**
   * Return the configured token expiration time in milliseconds.
   */
  @Override
  public long getExpirationTime() {
    return jwtExpiration;
  }
}