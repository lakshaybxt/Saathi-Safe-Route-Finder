package com.saathi.saathi_be.controller;

import com.saathi.saathi_be.domain.GeoLocation;
import com.saathi.saathi_be.domain.dto.request.AddressRequestDto;
import com.saathi.saathi_be.domain.dto.request.SafeRouteRequestDto;
import com.saathi.saathi_be.domain.dto.response.SafeRouteResponseDto;
import com.saathi.saathi_be.service.GeoLocationService;
import com.saathi.saathi_be.service.SafeRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/route")
@RequiredArgsConstructor
public class SafeRouteController {

    private final GeoLocationService geoLocationService;
    private final SafeRouteService safeRouteService;

    @PostMapping(path = "/safe")
    public ResponseEntity<SafeRouteResponseDto> getSafeRoute(@RequestBody AddressRequestDto request) {
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
}
