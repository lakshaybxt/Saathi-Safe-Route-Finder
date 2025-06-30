package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.request.SafeRouteRequestDto;
import com.saathi.saathi_be.domain.dto.response.SafeRouteResponseDto;

public interface SafeRouteService {
    SafeRouteResponseDto generateSafeRoute(SafeRouteRequestDto request);
}
