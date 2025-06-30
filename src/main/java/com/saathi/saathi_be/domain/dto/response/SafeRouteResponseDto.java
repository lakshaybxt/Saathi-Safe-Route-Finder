package com.saathi.saathi_be.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SafeRouteResponseDto {
    private List<RoutePoint> route;
    private RouteSummaryDto summary;
    private List<String> tips;
}
