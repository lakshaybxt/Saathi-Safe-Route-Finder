package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.GeoLocation;
import com.saathi.saathi_be.domain.entity.Address;

public interface GeoLocationService {
    GeoLocation geoLocate(Address address);
}
