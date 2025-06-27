package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.dto.RegisterUserDto;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.repository.UserRepository;
import com.saathi.saathi_be.service.AuthenticationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public User signup(RegisterUserDto register) {
        User user = User.builder()
                .username(register.getUsername())
                .email(register.getEmail())
                .password(encoder.encode(register.getPassword()))
                .verificationCode(generateVerificationCode())
                .verificationCodeExpiration(LocalDateTime.now().plusMinutes(15))
                .build();

        User savedUser = userRepository.save(user);

        sendVerificationEmail(user);

        return savedUser;
    }

    private void sendVerificationEmail(User user) {
        String subject = "Verify your email";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #1c1c1c; color: #f0f0f0; margin: 0; padding: 0;'>" +
                "<div style='max-width: 600px; margin: auto; padding: 30px; background-color: #2b2b2b; border-radius: 8px;'>" +
                "<h2 style='color: #00ffaa;'>🛡️ Welcome to Saathi – Your Safety Companion</h2>" +
                "<p style='font-size: 16px; color: #cccccc;'>To proceed securely, please use the verification code below:</p>" +

                "<div style='margin: 20px 0; padding: 20px; background-color: #000000; border-left: 5px solid #00ffaa; border-radius: 5px;'>" +
                "<h3 style='margin: 0; color: #ffffff;'>🔐 Your Verification Code:</h3>" +
                "<p style='font-size: 24px; font-weight: bold; color: #00ffaa; margin-top: 10px;'>" + verificationCode + "</p>" +
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
