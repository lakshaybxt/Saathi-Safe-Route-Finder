package com.saathi.saathi_be.domain.dto;

import com.saathi.saathi_be.domain.entity.Address;
import com.saathi.saathi_be.domain.entity.Place;
import com.saathi.saathi_be.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestimonialDto {

    @NotNull(message = "rating cannot be null")
    @Min(value = 1, message = "rating must be at least 1")
    @Max(value = 5, message = "rating must be at most 5")
    private int rating;

    @NotBlank(message = "comment is required")
    @Size(min = 10, max = 50000, message = "comment must be between {min} and {max} characters")
    private String comment;

    private String tips;

    @NotNull(message = "placeId is required")
    private UUID placeId;
}
