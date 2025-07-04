package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.GeoLocation;
import com.saathi.saathi_be.domain.dto.AddressDto;
import com.saathi.saathi_be.domain.entity.Place;
import com.saathi.saathi_be.exceptions.GeoLocationException;
import com.saathi.saathi_be.repository.PlaceRepository;
import com.saathi.saathi_be.service.GeoLocationService;
import com.saathi.saathi_be.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlacesServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public List<Place> getALlPlaces() {
        return placeRepository.findAllByOrderByLocalityAsc();
    }

    @Override
    public Place saveLocation(AddressDto addressDto) {
        Optional<Place> existingPlace = placeRepository
                .findPlaceByLocalityAndState(addressDto.getLocality(), addressDto.getState());

        if(existingPlace.isPresent()) {
            return existingPlace.get();
        }

        GeoLocation location = geoLocationService.geoLocate(addressDto);

        if(location == null) {
            throw new GeoLocationException("Failed to determine the coordinates from address.");
        }

        Place place = Place.builder()
                .locality(addressDto.getLocality()) // Make more detailed locality
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .riskColor("gray")
                .build();

        return placeRepository.save(place);
    }
}
