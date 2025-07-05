package com.saathi.saathi_be.service;

import com.saathi.saathi_be.domain.dto.EmergencyContactDto;
import com.saathi.saathi_be.domain.entity.EmergencyContact;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface EmergencyContactService {
    EmergencyContact createContact(EmergencyContactDto contact, UUID userId);
    EmergencyContact getContactById(UUID id);
    void deleteContact(UUID id);
    List<EmergencyContact> getAllContactsByUser(UUID userId);
    EmergencyContact updateContact(UUID id, @Valid EmergencyContactDto contact);
}
