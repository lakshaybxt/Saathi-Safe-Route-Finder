package com.saathi.auth.security;

import com.saathi.auth.domain.entity.User;
import com.saathi.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.UUID;

/**
 * Security filter that validates JWT tokens on incoming requests. If a valid
 * token is present the filter loads UserDetails and sets the Spring Security
 * context. Additionally, when the underlying UserDetails implementation is
 * {@link AuthUserDetails} the filter writes the user's id into the
 * request attribute "userId" for tenant-aware controllers.
 */
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    public final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);

            if(token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Claims claims = jwtService.extractAllClaims(token);

            String email = claims.getSubject();
            UUID userId = claims.get("userId", UUID.class);
            String username = claims.get("username", String.class);
            boolean enabled = claims.get("enabled", Boolean.class);

            if(email == null || !jwtService.validateToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .enabled(enabled)
                .build();

            AuthUserDetails userDetails = new AuthUserDetails(user);
            request.setAttribute("userId", userId);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            );

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ex) {
            exceptionResolver.resolveException(request, response, null, ex);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        return (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7)
                : null;
    }
}
