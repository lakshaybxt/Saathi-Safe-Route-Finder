package com.saathi.saathi_be.domain.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SosEventDto {

    @NotBlank(message = "Message is required")
    @Size(min = 3, max = 50000, message = "Content must be between {min} and {max} characters")
    private String message;

    @NotNull(message = "Latitude Required")
    private double latitude;

    @NotNull(message = "Longitude Required")
    private double longitude;

    private String deviceStatus;
}
