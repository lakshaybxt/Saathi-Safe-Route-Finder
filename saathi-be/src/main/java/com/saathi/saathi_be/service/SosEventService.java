package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.SosEventDto;
import com.saathi.saathi_be.domain.entity.SosEvent;

import java.util.UUID;

public interface SosEventService {
    SosEvent saveSosEvent(SosEventDto sosEventDto, UUID userId);
}
