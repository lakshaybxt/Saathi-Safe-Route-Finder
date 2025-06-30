package com.saathi.saathi_be.domain.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteSummaryDto {
    private String totalDistance;
    private String estimatedTime;
    private String riskScore;
    private int redZone;
    private int yellowZone;
    private String streetLights;
    private String crowdDensity;
}
