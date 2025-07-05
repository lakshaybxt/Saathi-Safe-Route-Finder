package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.dto.request.ResetPasswordRequest;
import com.saathi.saathi_be.domain.dto.request.UpdatePasswordRequest;
import com.saathi.saathi_be.domain.dto.request.UpdateUserRequest;
import com.saathi.saathi_be.domain.dto.request.UserEmailRequest;
import com.saathi.saathi_be.domain.dto.response.UserProfileResponse;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.repository.UserRepository;
import com.saathi.saathi_be.service.AuthenticationService;
import com.saathi.saathi_be.service.UserService;
import com.saathi.saathi_be.utility.VerificationCodeUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationService authenticationService;

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    @Override
    public UserProfileResponse updateUser(UpdateUserRequest request, User user) {
        user.setGender(request.getGender());
        user.setState(request.getState());
        user.setUsername(request.getUsername());

        userRepository.save(user);

        return UserProfileResponse.builder()
                .gender(user.getGender())
                .state(user.getState())
                .username(user.getUsername())
                .build();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }


    @Override
    public void updatePassword(UpdatePasswordRequest request, UUID userId) {
        User existingUser = getUserById(userId);

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

    @Override
    public void forgetPassword(UserEmailRequest request) {
        User existingUser = getUserByEmail(request.getEmail());

        if (existingUser == null) {
            throw new IllegalArgumentException("No user found with the requested mail: " + request.getEmail());
        }

        existingUser.setEnabled(false);
        existingUser.setVerificationCode(VerificationCodeUtil.generateVerificationCode());
        existingUser.setVerificationCodeExpiration(LocalDateTime.now().plusMinutes(15));
        userRepository.save(existingUser);
        authenticationService.sendVerificationEmail(existingUser);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

    }
}
