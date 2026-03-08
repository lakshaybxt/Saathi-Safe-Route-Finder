package com.saathi.auth.config;

import com.saathi.auth.security.JwtSecurityFilter;
import com.saathi.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

/**
 * Spring Security configuration for the application. Configures stateless JWT
 * authentication, CORS, and registers the {@link JwtSecurityFilter} in the
 * filter chain.
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationProvider authenticationProvider;

  /**
   * Configures the security filter chain for the application.
   *
   * @param http              the HttpSecurity object to configure
   * @param jwtSecurityFilter the JWT security filter
   * @return the configured SecurityFilterChain
   * @throws Exception if an error occurs while configuring the security filter chain
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtSecurityFilter jwtSecurityFilter) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/users/**").permitAll()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs.yaml").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  /**
   * Provides the JWT security filter bean.
   *
   * @param userDetailsService       the user details service
   * @param authenticationService    the JWT authentication service
   * @param handlerExceptionResolver the exception resolver for handling JWT exceptions
   * @return the JwtSecurityFilter bean
   */
  @Bean
  public JwtSecurityFilter jwtSecurityFilter(UserDetailsService userDetailsService,
                                             JwtService authenticationService,
                                             HandlerExceptionResolver handlerExceptionResolver
  ) {
    return new JwtSecurityFilter(userDetailsService, authenticationService, handlerExceptionResolver);
  }

  /**
   * Configures CORS settings for the application.
   *
   * @return the CorsConfigurationSource for the application
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(
        "https://backend.com",
        "http://localhost:5500",
        "http://127.0.0.1:8000",
        "http://localhost:8080",
        "http://localhost:3000"
    ));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-type"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
