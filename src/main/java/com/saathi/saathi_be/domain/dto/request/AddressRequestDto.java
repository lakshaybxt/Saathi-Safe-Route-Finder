package com.saathi.saathi_be.domain.dto.request;

import com.saathi.saathi_be.domain.dto.AddressDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequestDto {
    private AddressDto source;
    private AddressDto destination;
    private String mode;
}
