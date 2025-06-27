package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.RegisterUserDto;
import com.saathi.saathi_be.domain.entity.User;
import jakarta.validation.Valid;

public interface AuthenticationService {
    User signup(@Valid RegisterUserDto registerUserDto);
}
