package com.saathi.auth.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyUserDto {

    @NotBlank(message = "Verification code is required")
    private String verificationCode;

    @Email(message = "Email format is not valid")
    @NotBlank(message = "Email is required")
    private String email;
}
