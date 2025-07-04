package com.saathi.saathi_be.domain.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {

    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between {max} and {min} characters")
    private String password;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "State is required")
    private String state;
}
