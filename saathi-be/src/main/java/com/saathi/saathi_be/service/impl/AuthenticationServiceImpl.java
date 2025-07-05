package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.dto.LoginUserDto;
import com.saathi.saathi_be.domain.dto.RegisterUserDto;
import com.saathi.saathi_be.domain.dto.VerifyUserDto;
import com.saathi.saathi_be.domain.dto.request.UpdatePasswordRequest;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.exceptions.UserAlreadyExistsException;
import com.saathi.saathi_be.repository.UserRepository;
import com.saathi.saathi_be.service.AuthenticationService;
import com.saathi.saathi_be.service.EmailService;
import com.saathi.saathi_be.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    @Override
    public User signup(RegisterUserDto register) {

        if (userRepository.existsByEmail(register.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + register.getEmail() + " already exists");
        }

        if (userRepository.existsByUsername(register.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + register.getUsername() + " already exists");
        }

        User user = User.builder()
                .username(register.getUsername())
                .email(register.getEmail())
                .password(encoder.encode(register.getPassword()))
                .gender(register.getGender())
                .state(register.getState())
                .verificationCode(generateVerificationCode())
                .verificationCodeExpiration(LocalDateTime.now().plusMinutes(15))
                .build();

        User savedUser = userRepository.save(user);

        sendVerificationEmail(user);

        return savedUser;
    }

    @Override
    public UserDetails authenticate(LoginUserDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with email"));

        if(!user.isEnabled()) {
            throw new RuntimeException("Account not verified yet. Please verify you account");
        }

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return userDetailsService.loadUserByUsername(request.getEmail());
    }

    @Override
    public void verifyUser(VerifyUserDto request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getVerificationCodeExpiration().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has been expired");
            }
            if(user.getVerificationCode().equals(request.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiration(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void resendVerificationEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiration(LocalDateTime.now().plusMinutes(15));
            userRepository.save(user);
            sendVerificationEmail(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request, UUID userId) {
        User existingUser = userService.getUserById(userId);

        if (!encoder.matches(request.getOldPassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("Old password does not match");
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new IllegalArgumentException("New password cannot be same as old password");
        }

        String newHashPassword = encoder.encode(request.getNewPassword());
        existingUser.setPassword(newHashPassword);
        userRepository.save(existingUser);
    }

    private void sendVerificationEmail(User user) {
        String subject = "Verify your email";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #1c1c1c; color: #f0f0f0; margin: 0; padding: 0;'>" +
                "<div style='max-width: 600px; margin: auto; padding: 30px; background-color: #2b2b2b; border-radius: 8px;'>" +
                "<h2 style='color: #52B2E5;'>🛡️ Welcome to Saathi – Your Safety Companion</h2>" +
                "<p style='font-size: 16px; color: #cccccc;'>To proceed securely, please use the verification code below:</p>" +

                "<div style='margin: 20px 0; padding: 20px; background-color: #000000; border-left: 5px solid #52B2E5; border-radius: 5px;'>" +
                "<h3 style='margin: 0; color: #ffffff;'>🔐 Your Verification Code:</h3>" +
                "<p style='font-size: 24px; font-weight: bold; color: #52B2E5; margin-top: 10px;'>" + verificationCode + "</p>" +
                "</div>" +

                "<p style='font-size: 14px; color: #999;'>If you did not request this code, you can safely ignore this email.</p>" +
                "<p style='font-size: 14px; color: #999;'>Stay alert, stay safe. <br/>– Team Saathi 🧭</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
