package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.dto.RegisterUserDto;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public User signup(RegisterUserDto registerUserDto) {
        return null;
    }
}
