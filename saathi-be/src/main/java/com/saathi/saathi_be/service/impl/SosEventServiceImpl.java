package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.dto.SosEventDto;
import com.saathi.saathi_be.domain.entity.EmergencyContact;
import com.saathi.saathi_be.domain.entity.SosEvent;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.repository.EmergencyContactRepository;
import com.saathi.saathi_be.repository.SosEventRepository;
import com.saathi.saathi_be.service.SmsService;
import com.saathi.saathi_be.service.SosEventService;
import com.saathi.saathi_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SosEventServiceImpl implements SosEventService {

    private final SosEventRepository sosEventRepository;
    private final UserService userService;
    private final SmsService smsService;
    private final EmergencyContactRepository emergencyContactRepository;

    @Override
    public SosEvent sendSosEvent(SosEventDto sosEventDto, UUID userId) {
        User user = userService.getUserById(userId);

        SosEvent event = SosEvent.builder()
                .user(user)
                .message(sosEventDto.getMessage())
                .latitude(sosEventDto.getLatitude())
                .longitude(sosEventDto.getLongitude())
                .deviceStatus(sosEventDto.getDeviceStatus())
                .build();

        String message = String.format(
                "🚨 SOS Alert! 🚨\n\nMessage: %s\nLocation: Latitude %.6f, Longitude %.6f\nDevice Status: %s\n\nPlease respond immediately.",
                sosEventDto.getMessage(),
                sosEventDto.getLatitude(),
                sosEventDto.getLongitude(),
                sosEventDto.getDeviceStatus() != null ? sosEventDto.getDeviceStatus() : "Unknown"
        );

        List<EmergencyContact> contacts = emergencyContactRepository.findALlByUserId(userId);
        List<String> numbers = contacts.stream()
                .map(contact -> "+91" + contact.getPhoneNo())
                .toList();

        for(String to : numbers) {
            smsService.sendSms(to, message);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted while waiting between SMS sends", e);
            }
        }

        return sosEventRepository.save(event);
    }
}
