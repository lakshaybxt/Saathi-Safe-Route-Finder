package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.LoginUserDto;
import com.saathi.saathi_be.domain.dto.RegisterUserDto;
import com.saathi.saathi_be.domain.dto.VerifyUserDto;
import com.saathi.saathi_be.domain.dto.request.UpdatePasswordRequest;
import com.saathi.saathi_be.domain.entity.User;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface AuthenticationService {
    User signup(@Valid RegisterUserDto register);
    UserDetails authenticate(@Valid LoginUserDto request);
    void verifyUser(@Valid VerifyUserDto request);
    void resendVerificationEmail(String email);
    void sendVerificationEmail(User user);
}
