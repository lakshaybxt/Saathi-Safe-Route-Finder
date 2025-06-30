package com.saathi.saathi_be.domain.dto.request;

import com.saathi.saathi_be.domain.dto.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequestDto {

    @Valid
    private AddressDto source;

    @Valid
    private AddressDto destination;

    @NotBlank(message = "Country is required")
    private String mode;
}
