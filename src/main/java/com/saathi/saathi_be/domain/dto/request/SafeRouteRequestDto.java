package com.saathi.saathi_be.domain.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SafeRouteRequestDto {
    private String source;
    private String destination;
    private String mode;
}
