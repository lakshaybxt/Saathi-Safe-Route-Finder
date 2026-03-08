package com.saathi.auth.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {

  @NotBlank(message = "Username is required")
  private String username;

  @Email(message = "Email format is not valid")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
    private String password;
}
