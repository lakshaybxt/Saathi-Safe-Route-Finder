package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.RegisterUserDto;
import com.saathi.saathi_be.domain.dto.request.UpdateUserRequest;
import com.saathi.saathi_be.domain.dto.response.UserProfileResponse;
import com.saathi.saathi_be.domain.entity.User;
import jakarta.validation.Valid;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID userId);
    UserProfileResponse updateUser(UpdateUserRequest request, User user);
}
