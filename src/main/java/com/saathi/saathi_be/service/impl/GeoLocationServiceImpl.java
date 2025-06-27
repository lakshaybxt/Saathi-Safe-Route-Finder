package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.GeoLocation;
import com.saathi.saathi_be.domain.entity.Address;
import com.saathi.saathi_be.service.GeoLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeoLocationServiceImpl implements GeoLocationService {

    private final RestTemplate restTemplate;

    @Override
    public GeoLocation geoLocate(Address address) {
        return null;
    }
}
