package com.saathi.saathi_be.controller;

import com.saathi.saathi_be.domain.GeoLocation;
import com.saathi.saathi_be.domain.dto.request.AddressRequestDto;
import com.saathi.saathi_be.domain.dto.request.SafeRouteRequestDto;
import com.saathi.saathi_be.domain.dto.response.RiskSummaryResponse;
import com.saathi.saathi_be.domain.dto.response.SafeRouteResponseDto;
import com.saathi.saathi_be.service.GeoLocationService;
import com.saathi.saathi_be.service.SafeRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/route")
@RequiredArgsConstructor
public class SafeRouteController {

    private final GeoLocationService geoLocationService;
    private final SafeRouteService safeRouteService;

    @PostMapping(path = "/safe")
    public ResponseEntity<SafeRouteResponseDto> getSafeRoute(@Valid @RequestBody AddressRequestDto request) {
        GeoLocation source = geoLocationService.geoLocate(request.getSource());
        GeoLocation destination = geoLocationService.geoLocate(request.getDestination());

        SafeRouteRequestDto requestDto = SafeRouteRequestDto.builder()
                .source(source.toString())
                .destination(destination.toString())
                .mode(request.getMode())
                .build();

        SafeRouteResponseDto response = safeRouteService.generateSafeRoute(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/risk/summary")
    public ResponseEntity<List<RiskSummaryResponse>> getRiskSummaryByCity(
            @RequestParam(required = true) String state) {

        if(state == null || state.isBlank()) {
            return ResponseEntity.badRequest().body(List.of());
        }

        state = state.trim();
        List<RiskSummaryResponse> response = safeRouteService.countRiskColorByState(state);

       if(response.isEmpty()) {
           return ResponseEntity.noContent().build();
       }

        return ResponseEntity.ok(response);
    }
}
