package com.saathi.saathi_be.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saathi.saathi_be.domain.GeoLocation;
import com.saathi.saathi_be.domain.entity.Address;
import com.saathi.saathi_be.exceptions.GeoLocationException;
import com.saathi.saathi_be.service.GeoLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class GeoLocationServiceImpl implements GeoLocationService {

    private final RestTemplate restTemplate;

    @Override
    public GeoLocation geoLocate(Address address) {
        try {
            String fullAddress = String.format("%s %s %s %s %s",
                    address.getLocality(),
                    address.getCity(),
                    address.getState(),
                    address.getPostalCode(),
                    address.getCountry()
            ).replaceAll("null", "").replaceAll("\\s{2,}", " ").trim();

            String encodedAddress = URLEncoder.encode(fullAddress, StandardCharsets.UTF_8);

            String url = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json&limit=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "SpringBootApp/1.0");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            if(!root.isArray() || root.isEmpty()) {
                throw new GeoLocationException("No Location found for the address");
            }

            JsonNode location = root.get(0);

            if(!location.has("lat") || !location.has("lon")) {
                throw new GeoLocationException("Location data is incomplete from geoLocation API");
            }

            double lat = location.get("lat").asDouble();
            double lon = location.get("lon").asDouble();

            return GeoLocation.builder()
                    .latitude(lat)
                    .longitude(lon)
                    .build();
        } catch (Exception e) {
            throw new GeoLocationException("Failed to resolve geolocation", e);
        }
        return null;
    }
}
