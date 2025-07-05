package com.saathi.saathi_be.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Contact Number is required")
    @Size(min = 10, max = 10, message = "Phone Number must be of {max} digits")
    private String phoneNo;

    private String relation;

}
