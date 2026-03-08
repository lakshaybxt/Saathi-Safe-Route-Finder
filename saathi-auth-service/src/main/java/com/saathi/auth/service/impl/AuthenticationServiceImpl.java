package com.saathi.auth.service.impl;

import com.saathi.auth.domain.dto.request.LoginUserDto;
import com.saathi.auth.domain.dto.request.RegisterUserDto;
import com.saathi.auth.domain.dto.request.VerifyUserDto;
import com.saathi.auth.domain.entity.User;
import com.saathi.auth.kafka.events.EmailEvent;
import com.saathi.auth.repos.UserRepository;
import com.saathi.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.saathi.auth.kafka.topics.KafkaTopics.NOTIFICATION_EMAIL_SEND;

/**
 * Service responsible for user authentication operations including signup,
 * authentication and verification. This implementation adds structured
 * logging and timing (StopWatch) to help observability similar to other
 * service implementations in the project. Micrometer usage was intentionally
 * removed as requested — StopWatch is used for duration measurements.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final KafkaTemplate<String, EmailEvent> kafkaTemplate;

  /**
   * Registers a new user account. The returned user will have enabled=false
   * and will contain a verification code with a short expiry.
   *
   * @param request registration details (email, username, password)
   * @return the saved User entity
   * @throws IllegalArgumentException when email or username already exists
   */
  @Override
  public User signup(RegisterUserDto request) {
    StopWatch sw = new StopWatch();
    sw.start();

    try {
      log.debug("action=signup email={} username={}", request.getEmail(), request.getUsername());

      if (userRepository.existsByEmail(request.getEmail())) {
        throw new IllegalArgumentException("Email already in use");
      }

      if (userRepository.existsByUsername(request.getUsername())) {
        throw new IllegalArgumentException("Username already in use");
      }

      User user = User.builder()
          .email(request.getEmail())
          .username(request.getUsername())
          .password(encoder.encode(request.getPassword()))
          .enabled(false)
          .verificationCode(generateVerificationCode())
          .verificationCodeExpiration(LocalDateTime.now().plusMinutes(15))
          .build();

      User saved = userRepository.save(user);
      EmailEvent event = EmailEvent.builder()
          .userId(saved.getId())
          .to(saved.getEmail())
          .subject("Verify your account")
          .body(String.format("Your verification code is: %s", saved.getVerificationCode()))
          .build();

      kafkaTemplate.send(NOTIFICATION_EMAIL_SEND,
          saved.getId().toString(), event).whenComplete((result, ex) -> {
        if (ex != null) {
          log.error("action=email_event_publish_failed userId={} error={}", saved.getId(), ex.getMessage());
        } else {
          log.debug("action=email_event_published topic={} partition={} offset={}",
              result.getRecordMetadata().topic(),
              result.getRecordMetadata().partition(),
              result.getRecordMetadata().offset());
        }
      });

      sw.stop();
      UUID userId = saved != null ? saved.getId() : null;
      log.info("action=signup email={} userId={} durationMs={}", request.getEmail(), userId, sw.getTotalTimeMillis());
      return saved;
    } catch (IllegalArgumentException e) {
      if (sw.isRunning()) sw.stop();
      log.warn("action=signup email={} error={}", request.getEmail(), e.getMessage());
      throw e;
    } catch (Exception e) {
      if (sw.isRunning()) sw.stop();
      log.error("action=signup email={} error={}", request.getEmail(), e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Authenticates a user with email and password. The returned UserDetails
   * can be used to create a session or JWT.
   *
   * @param request login credentials (email, password)
   * @return authenticated UserDetails
   * @throws IllegalArgumentException when the email is invalid or user not verified
   * @throws BadCredentialsException  when credentials are invalid
   */
  @Override
  public UserDetails authenticate(LoginUserDto request) {
    StopWatch sw = new StopWatch();
    sw.start();

    try {
      log.debug("action=authenticate email={}", request.getEmail());

      User existingUser = userRepository.findByEmail(request.getEmail())
          .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

      if (!existingUser.isEnabled()) {
        throw new IllegalArgumentException("Email not verified");
      }

      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
      );

      UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

      sw.stop();
      String username = userDetails != null ? userDetails.getUsername() : null;
      log.info("action=authenticate email={} username={} durationMs={}", request.getEmail(), username, sw.getTotalTimeMillis());
      return userDetails;
    } catch (BadCredentialsException e) {
      if (sw.isRunning()) sw.stop();
      log.warn("action=authenticate email={} error={}", request.getEmail(), e.getMessage());
      throw new BadCredentialsException("Invalid username or password");
    } catch (IllegalArgumentException e) {
      if (sw.isRunning()) sw.stop();
      log.warn("action=authenticate email={} error={}", request.getEmail(), e.getMessage());
      throw e;
    } catch (Exception e) {
      if (sw.isRunning()) sw.stop();
      log.error("action=authenticate email={} error={}", request.getEmail(), e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Verifies a user's account using the verification code sent during signup.
   *
   * @param request verification payload (email, verificationCode)
   * @throws IllegalArgumentException when code is invalid or expired or user already verified
   */
  @Override
  public void verifyUser(VerifyUserDto request) {
    StopWatch sw = new StopWatch();
    sw.start();

    try {
      log.debug("action=verifyUser email={}", request.getEmail());

      Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

      if (optionalUser.isPresent()) {
        User user = optionalUser.get();

        if (user.isEnabled()) {
          throw new IllegalArgumentException("User already verified");
        }

        if (user.getVerificationCodeExpiration() == null || user.getVerificationCodeExpiration().isBefore(LocalDateTime.now()))
          throw new IllegalArgumentException("Verification code expired");

        if (user.getVerificationCode() != null && user.getVerificationCode().equals(request.getVerificationCode())) {
          user.setEnabled(true);
          user.setVerificationCode(null);
          user.setVerificationCodeExpiration(null);
          userRepository.save(user);
        } else {
          throw new IllegalArgumentException("Invalid verification code");
        }

      } else {
        throw new RuntimeException("User not found");
      }

      sw.stop();
      log.info("action=verifyUser email={} durationMs={}", request.getEmail(), sw.getTotalTimeMillis());
    } catch (IllegalArgumentException e) {
      if (sw.isRunning()) sw.stop();
      log.warn("action=verifyUser email={} error={}", request.getEmail(), e.getMessage());
      throw e;
    } catch (Exception e) {
      if (sw.isRunning()) sw.stop();
      log.error("action=verifyUser email={} error={}", request.getEmail(), e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Generates a random 6-digit verification code as a string.
   *
   * @return 6-digit verification code
   */
  private String generateVerificationCode() {
    int codeLength = 6;
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < codeLength; i++) {
      int digit = (int) (Math.random() * 10);
      code.append(digit);
    }
    return code.toString();
  }

}
