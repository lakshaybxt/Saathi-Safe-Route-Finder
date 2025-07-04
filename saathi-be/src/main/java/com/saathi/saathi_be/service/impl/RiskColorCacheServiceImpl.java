package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.repository.PlaceRepository;
import com.saathi.saathi_be.service.RiskColorCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RiskColorCacheServiceImpl implements RiskColorCacheService {

    private final PlaceRepository placeRepository;
    // todo: GIVE THIS PART IN DB FOR BATCH SEARCH
    @Override
    @Cacheable("riskColorByCity")
    public String getRiskColorByCity(String locality) {
        String[] parts = Arrays.stream(locality.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toArray(String[]::new);
        String color = placeRepository.findRiskColorByLocalityContaining(parts);
        return color != null ? color : "gray";
    }
}
