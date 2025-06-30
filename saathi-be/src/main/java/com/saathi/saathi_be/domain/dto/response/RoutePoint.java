package com.saathi.saathi_be.domain.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoutePoint {
    private double lat;
    private double lon;
    private String city;
    private String color;
}
