package com.saathi.saathi_be.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.saathi.saathi_be.domain.dto.response.RoutePoint;
import com.saathi.saathi_be.domain.dto.response.RouteSummaryDto;

import java.time.LocalTime;
import java.util.List;

public class RouteSummaryUtil {

    public static RouteSummaryDto buildRouteSummary(JsonNode orsRoot, List<RoutePoint> routePoints) {
        JsonNode summaryNode = orsRoot.path("routes").get(0).path("summary");
        double distance = summaryNode.path("distance").asDouble();
        int duration = summaryNode.path("duration").asInt();

        int redZone = 0;
        int yellowZone = 0;
        int greenZone = 0;

        for(var point : routePoints) {
            if("red".equalsIgnoreCase(point.getColor())) {
                redZone++;
            } else if ("yellow".equalsIgnoreCase(point.getColor())) {
                yellowZone++;
            } else if ("green".equalsIgnoreCase(point.getColor())) {
                greenZone++;
            }
        }

        String riskScore = computeRiskScore(redZone, yellowZone, routePoints.size());
        
        return RouteSummaryDto.builder()
                .totalDistance(formatDistance(distance))
                .estimatedTime(formatDuration(duration))
                .riskScore(riskScore)
                .redZone(redZone)
                .yellowZone(yellowZone)
                .streetLights("No Data Available")
                .crowdDensity(computeCrowdDensity())
                .build();
    }

    private static String computeCrowdDensity() {
        LocalTime now = LocalTime.now();

        if (now.isAfter(LocalTime.of(6, 0)) && now.isBefore(LocalTime.of(12, 0))) {
            return "Moderate";
        } else if (now.isAfter(LocalTime.of(12, 0)) && now.isBefore(LocalTime.of(18, 0))) {
            return "Crowded";
        } else if (now.isAfter(LocalTime.of(18, 0)) && now.isBefore(LocalTime.of(22, 0))) {
            return "Less - Crowded";
        } else {
            return "Not crowded";
        }
    }

    private static String computeRiskScore(int redZone, int yellowZone, int size) {
        if(size == 0) return "Unknown";

        if(redZone >= size / 2) return "High";
        else if((redZone + yellowZone) >= size / 2) return "Moderate";
        else return "Low";
    }

    private static String formatDuration(int duration) {
        int hours = duration / 3600;
        int minutes = (duration / 60) % 60; // Modulo 60 will return the remaining minutes

        if(hours > 0) {
            return String.format("%d hr %d min", hours, minutes);
        } else {
            return String.format("%d min", minutes);
        }
    }

    private static String formatDistance(double distance) {
        if(distance >= 1000) {
            return String.format("%.1f km", distance / 1000);
        } else {
            return String.format("%.0f m", distance);
        }
    }
}
