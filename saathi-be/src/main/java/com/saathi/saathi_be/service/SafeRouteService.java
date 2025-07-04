package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.request.SafeRouteRequestDto;
import com.saathi.saathi_be.domain.dto.response.RiskSummaryResponse;
import com.saathi.saathi_be.domain.dto.response.SafeRouteResponseDto;

import java.util.List;

public interface SafeRouteService {
    SafeRouteResponseDto generateSafeRoute(SafeRouteRequestDto request);
    List<RiskSummaryResponse> countRiskColorByCity(String city);
}
