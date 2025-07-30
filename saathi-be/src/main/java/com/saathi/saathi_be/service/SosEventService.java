package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.SosEventDto;
import com.saathi.saathi_be.domain.entity.SosEvent;
import jakarta.validation.Valid;

import java.util.UUID;

public interface SosEventService {
    SosEvent sendSosEvent(SosEventDto sosEventDto, UUID userId);
}
