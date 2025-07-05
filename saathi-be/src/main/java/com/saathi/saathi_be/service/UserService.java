package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.RegisterUserDto;
import com.saathi.saathi_be.domain.dto.request.ResetPasswordRequest;
import com.saathi.saathi_be.domain.dto.request.UpdatePasswordRequest;
import com.saathi.saathi_be.domain.dto.request.UpdateUserRequest;
import com.saathi.saathi_be.domain.dto.request.UserEmailRequest;
import com.saathi.saathi_be.domain.dto.response.UserProfileResponse;
import com.saathi.saathi_be.domain.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID userId);
    UserProfileResponse updateUser(UpdateUserRequest request, User user);
    User getUserByEmail(String email);
    void updatePassword(UpdatePasswordRequest request, UUID userId);
    void forgetPassword(UserEmailRequest request);
    void resetPassword(ResetPasswordRequest request);
}
