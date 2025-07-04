package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.AddressDto;
import com.saathi.saathi_be.domain.entity.Place;
import jakarta.validation.Valid;

import java.util.List;

public interface PlaceService {
    List<Place> getALlPlaces();
    Place saveLocation(@Valid AddressDto addressDto);
    List<Place> getHighRiskAreaByState(String color, String state);
}
