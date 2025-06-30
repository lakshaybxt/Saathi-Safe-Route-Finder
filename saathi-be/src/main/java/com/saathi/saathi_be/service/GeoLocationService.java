package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.GeoLocation;
import com.saathi.saathi_be.domain.dto.AddressDto;

public interface GeoLocationService {
    GeoLocation geoLocate(AddressDto address);
    String reverseGeocode(double lat, double lon);
}
