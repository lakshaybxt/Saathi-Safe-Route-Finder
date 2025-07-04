package com.saathi.saathi_be.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saathi.saathi_be.domain.GeoLocation;
import com.saathi.saathi_be.domain.dto.AddressDto;
import com.saathi.saathi_be.domain.entity.Address;
import com.saathi.saathi_be.exceptions.GeoLocationException;
import com.saathi.saathi_be.service.GeoLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GeoLocationServiceImpl implements GeoLocationService {

    private final RestTemplate restTemplate;

    @Override
    public GeoLocation geoLocate(AddressDto address) {
        try {
            String fullAddress = String.format("%s %s %s %s",
                    address.getLocality(),
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
    }

    @Override
    @Cacheable("cityFromLatLon")
    public String reverseGeocode(double lat, double lon) {
        try {
            String url = String.format(
                    "https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json", lat, lon
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "SpringBootApp/1.0");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            if(!root.has("address")) {
                throw new GeoLocationException("No address found for the given coordinates.");
            }

            JsonNode address = root.get("address");
// TODO: search in db by contain any of this (add the locality in one)
            String locality = Stream.of(
                    address.path("neighbourhood").asText(null),
                    address.path("suburb").asText(null),
                    address.path("quarter").asText(null),
                    address.path("locality").asText(null),
                    address.path("village").asText(null),
                    address.path("hamlet").asText(null),
                    address.path("county").asText(null),
                    address.path("amenity").asText(null)
            ).filter(s -> s != null && !s.isBlank())
            .collect(Collectors.joining(", ")
            ).replaceAll(",\\s*,", ", ").replaceAll("^,|,$", "").trim();
            // No use for city and state
//            String locationName = String.format("%s, %s, %s",
//                    locality,
//                    address.path("city").asText(""),
//                    address.path("state").asText("")
//                    ).replaceAll(",\\s*,", ", ").replaceAll("^,|,$", "").trim();

            if(locality.isBlank()) {
                throw new GeoLocationException("Incomplete location details from reverse geocoding");
            }

            return locality;

        } catch (Exception e) {
            throw new GeoLocationException("Failed to resolve geocode location", e);
        }
    }
}
