package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.entity.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID userId);
}
