package com.saathi.saathi_be.controller;

import com.saathi.saathi_be.domain.dto.AddressDto;
import com.saathi.saathi_be.domain.dto.PlaceDto;
import com.saathi.saathi_be.domain.entity.Place;
import com.saathi.saathi_be.mapper.PlaceMapper;
import com.saathi.saathi_be.service.GeoLocationService;
import com.saathi.saathi_be.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placesService;
    private final PlaceMapper placeMapper;
    private final GeoLocationService geoLocationService;

    @GetMapping
    public ResponseEntity<List<PlaceDto>> getAllPlaces() {
        List<Place> places = placesService.getALlPlaces();
        List<PlaceDto> placeDto = places.stream()
                .map(placeMapper::toDto)
                .toList();

        return ResponseEntity.ok(placeDto);
    }

    @PostMapping// Should return UUID
    public ResponseEntity<PlaceDto> saveLocation(@Valid @RequestBody AddressDto addressDto) {
        Place place = placesService.saveLocation(addressDto);
        PlaceDto placeDto = placeMapper.toDto(place);
        return ResponseEntity.ok(placeDto);
    }

    @GetMapping(path = "/highRisk")
    public ResponseEntity<List<PlaceDto>> getHighRiskArea(@RequestParam String state) {
        if(state == null || state.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Place> redZones = placesService.getHighRiskAreaByState("red", state);
        List<PlaceDto> placeDto = redZones.stream()
                .map(placeMapper::toDto)
                .toList();

        return ResponseEntity.ok(placeDto);
    }
}
