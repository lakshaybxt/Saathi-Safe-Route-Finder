package com.saathi.saathi_be.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.saathi.saathi_be.configuration.OrsConfig;
import com.saathi.saathi_be.domain.dto.request.SafeRouteRequestDto;
import com.saathi.saathi_be.domain.dto.response.RoutePoint;
import com.saathi.saathi_be.domain.dto.response.SafeRouteResponseDto;
import com.saathi.saathi_be.domain.entity.Testimonial;
import com.saathi.saathi_be.exceptions.RouteNotFoundException;
import com.saathi.saathi_be.exceptions.RouteParsingException;
import com.saathi.saathi_be.repository.TestimonialRepository;
import com.saathi.saathi_be.service.GeoLocationService;
import com.saathi.saathi_be.service.RiskColorCacheService;
import com.saathi.saathi_be.service.SafeRouteService;
import com.saathi.saathi_be.utility.PolylineDecoder;
import com.saathi.saathi_be.utility.RouteSummaryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafeRouteServiceImpl implements SafeRouteService {

    private static final int SAMPLING_INTERVAL = 10;
    private static final double GEO_API_RATE = 0.9;
    private static final String DEFAULT_PROFILE = "driving-car";

    private final RiskColorCacheService riskColorCacheService;
    private final TestimonialRepository testimonialRepository;

    private final RestTemplate restTemplate;
    private final OrsConfig orsConfig;
    private final GeoLocationService geoLocationService;

    @Override
    public SafeRouteResponseDto generateSafeRoute(SafeRouteRequestDto request) {
        String mode = request.getMode();
        String orsProfile = mapModeToOrsProfile(mode);
        String url = "https://api.openrouteservice.org/v2/directions/" + orsProfile;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", orsConfig.getApiKey());

        List<List<Double>> coordinates = List.of(
                parseCoordinates(request.getSource()),
                parseCoordinates(request.getDestination())
        );

        Map<String, Object> body = Map.of("coordinates", coordinates);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        System.out.println(body.toString());

        ResponseEntity<String> response = restTemplate.postForEntity(
                url, entity, String.class
        );

        String responseBody = response.getBody();
        if(responseBody == null || responseBody.isEmpty()) {
            throw new RouteParsingException("OpenRouteService returned empty response");
        }

        return parseRouteResponse(responseBody);
    }

    private String mapModeToOrsProfile(String mode) {
        if(mode == null || mode.isBlank()) {
            return DEFAULT_PROFILE;
        }
        // Mode should be given this only otherwise error
        return switch (mode.toLowerCase().trim()) {
            case "driving", "car", "auto", "vehicle" -> "driving-car";
            case "driving-hgv", "truck", "hgv" -> "driving-hgv";
            case "cycling", "bike", "bicycle" -> "cycling-regular";
            case "cycling-road", "road-bike" -> "cycling-road";
            case "cycling-mountain", "mountain-bike" -> "cycling-mountain";
            case "cycling-electric", "e-bike" -> "cycling-electric";
            case "walking", "foot", "pedestrian" -> "foot-walking";
            case "hiking", "trekking" -> "foot-hiking";
            case "wheelchair", "accessibility" -> "wheelchair";
            default -> {
                // Log warning about unknown mode
                System.err.println("Warning: Unknown mode '" + mode + "', using default profile: " + DEFAULT_PROFILE);
                yield DEFAULT_PROFILE;
            }
        };
    }

    private List<Double> parseCoordinates(String destination) {
        try {
            String[] parts = destination.split(",");
            if(parts.length != 2) throw new IllegalArgumentException("Invalid coordinates format.");
            double lon = Double.parseDouble(parts[0].trim());
            System.out.print(lon + " ");
            double lat = Double.parseDouble(parts[1].trim());
            System.out.println(lat);
            return List.of(lon, lat);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinates numbers.");
        }
    }

    private SafeRouteResponseDto parseRouteResponse(String responseBody) {
        String geometry = extractGeometry(responseBody);
        List<List<Double>> routeCoordinates = PolylineDecoder.decodePolyline(geometry);
        List<RoutePoint> routePoints = new ArrayList<>();

        // Caching
        Map<String, String> cityCache = new HashMap<>();

        // Unstable so might later I can change
        RateLimiter rateLimiter = RateLimiter.create(GEO_API_RATE); // ~ 1 call per second

        for(int i = 0; i < routeCoordinates.size(); i += SAMPLING_INTERVAL) {
            rateLimiter.acquire();

            if(i + SAMPLING_INTERVAL >= routeCoordinates.size()) {
                i = routeCoordinates.size() - 1;
            }

            List<Double> point = routeCoordinates.get(i);
            double lon = point.get(0);
            double lat = point.get(1);
            String key = lat + "," + lon;

            /*“If the key is not present in the map, then:
            Call mappingFunction.apply(key)
            Store the result as map.put(key, result)
            Return the result”*/
            // locationName = locality + city + state (e.g., Burari, Delhi, Delhi)
            String locationName = cityCache.computeIfAbsent(key, k ->
                    geoLocationService.reverseGeocode(lat, lon));
//            String locality = locationName.split(",")[0].trim();

            String color = riskColorCacheService.getRiskColorByCity(locationName); // if no data then gray

            RoutePoint routePoint = RoutePoint.builder()
                    .lat(lat)
                    .lon(lon)
                    .city(locationName)
                    .color(color)
                    .build();
            routePoints.add(routePoint);

            /*
            // For avoid rate limiting
            try {
                Thread.sleep(1100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
             */
        }
        List<String> cityNames = routePoints.stream()
                .map(RoutePoint::getCity)
                .filter(city -> city != null && !city.isBlank())
                .flatMap(city -> Arrays.stream(city.split(",")))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(part -> !part.isBlank())
                .distinct()
                .toList();

        String[] cityNameArray = cityNames.toArray(new String[0]);
        log.info("City Name Parts: {}", Arrays.toString(cityNameArray));

        List<Testimonial> testimonials = testimonialRepository
                .findTop5ByPlace_LocalityInOrderByRatingDesc(cityNameArray);

        log.info("Fetched Testimonials: {}", testimonials);
        log.info("Testimonial count: {}", testimonials.size());

        List<String> tips = testimonials.stream()
                .map(Testimonial::getTips)
                .filter(tip -> tip != null && !tip.isBlank())
                .limit(5)
                .toList();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode orsRoot = mapper.readTree(responseBody);
            return SafeRouteResponseDto.builder()
                    .route(routePoints)
                    .summary(RouteSummaryUtil.buildRouteSummary(orsRoot, routePoints))
                    .tips(tips)
                    .build(); // TODO: Also add the nearest safest place
        } catch (JsonProcessingException e) {
            throw new RouteParsingException("Failed to parse route JSON response.", e);
        }
    }

    public String extractGeometry(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            JsonNode routes = root.get("routes");

            if(!routes.isArray() || routes.isEmpty()) {
                throw new RouteNotFoundException("No route found in the response.");
            }

            return routes.get(0).path("geometry").asText();

        } catch (IOException e) {
            throw new RouteParsingException("Failed to parse route JSON response.", e);
        }
    }



}
