package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.dto.SosEventDto;
import com.saathi.saathi_be.domain.entity.SosEvent;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.repository.SosEventRepository;
import com.saathi.saathi_be.service.SosEventService;
import com.saathi.saathi_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SosEventServiceImpl implements SosEventService {

    private final SosEventRepository sosEventRepository;
    private final UserService userService;

    @Override
    public SosEvent saveSosEvent(SosEventDto sosEventDto, UUID userId) {
        User user = userService.getUserById(userId);

        SosEvent event = SosEvent.builder()
                .user(user)
                .message(sosEventDto.getMessage())
                .latitude(sosEventDto.getLatitude())
                .longitude(sosEventDto.getLongitude())
                .deviceStatus(sosEventDto.getDeviceStatus())
                .build();
        return sosEventRepository.save(event);
    }
}
