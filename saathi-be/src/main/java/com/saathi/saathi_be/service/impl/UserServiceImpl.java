package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.dto.RegisterUserDto;
import com.saathi.saathi_be.domain.dto.request.UpdateUserRequest;
import com.saathi.saathi_be.domain.dto.response.UserProfileResponse;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.repository.UserRepository;
import com.saathi.saathi_be.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
}
