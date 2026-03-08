package com.saathi.auth.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {

  @Email(message = "Email format is not valid")
  @NotBlank(message = "Email is required")
  private String email;

  @Size(min = 8, max = 20, message = "Password must be between {max} and {min} characters")
  private String password;
}