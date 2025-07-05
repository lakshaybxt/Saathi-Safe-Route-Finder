package com.saathi.saathi_be.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEmailRequest {

    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String email;
}
