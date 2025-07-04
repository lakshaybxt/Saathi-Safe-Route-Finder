package com.saathi.saathi_be.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceDto {
    private UUID id;
    private String locality;
    private double latitude;
    private double longitude;
    private String riskColor;
}
