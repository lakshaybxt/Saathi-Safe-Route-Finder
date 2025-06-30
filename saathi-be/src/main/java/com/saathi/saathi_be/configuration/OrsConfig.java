package com.saathi.saathi_be.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OrsConfig {

    @Value("${openrouteservice.api.key}")
    private String apiKey;
}
