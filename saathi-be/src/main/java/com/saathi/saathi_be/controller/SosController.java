package com.saathi.saathi_be.controller;

import com.saathi.saathi_be.domain.dto.SosEventDto;
import com.saathi.saathi_be.domain.dto.response.SafeRouteResponseDto;
import com.saathi.saathi_be.domain.dto.response.SosResponseDto;
import com.saathi.saathi_be.domain.entity.SosEvent;
import com.saathi.saathi_be.service.SosEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sos")
@RequiredArgsConstructor
public class SosController {

    private final SosEventService sosEventService;

    @PostMapping
    public ResponseEntity<SosResponseDto> storeSos(
            @Valid @RequestBody SosEventDto sosEventDto,
            @RequestAttribute UUID userId
    ) {
        SosEvent sosEvent = sosEventService.saveSosEvent(sosEventDto, userId);
        SosResponseDto responseDto = new SosResponseDto("SOS sent successfully", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
