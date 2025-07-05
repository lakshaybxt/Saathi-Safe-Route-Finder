package com.saathi.saathi_be.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between {max} and {min} characters")
    private String oldPassword;


    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between {max} and {min} characters")
    private String newPassword;
}
