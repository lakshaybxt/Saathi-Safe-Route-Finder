package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.domain.dto.EmergencyContactDto;
import com.saathi.saathi_be.domain.entity.EmergencyContact;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.repository.EmergencyContactRepository;
import com.saathi.saathi_be.service.EmergencyContactService;
import com.saathi.saathi_be.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmergencyContactServiceImpl implements EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;
    private final UserService userService;

    @Override
    public EmergencyContact createContact(EmergencyContactDto contact, UUID userId) {
        User user = userService.getUserById(userId);

        long count = emergencyContactRepository.countByUserId(userId);
        if(count >= 5) {
            throw new IllegalArgumentException("You can only add up to 5 emergency contacts");
        }
        boolean exists = emergencyContactRepository.existsByPhoneNoAndUserId(contact.getPhoneNo(), userId);

        if(exists) {
            throw new IllegalArgumentException("This contact already exists");
        }

        EmergencyContact emergencyContact = EmergencyContact.builder()
                .name(contact.getName())
                .phoneNo(contact.getPhoneNo())
                .relation(contact.getRelation())
                .user(user)
                .build();

        return emergencyContactRepository.save(emergencyContact);
    }

    @Override
    public EmergencyContact getContactById(UUID id) {
        return emergencyContactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));
    }

    @Override
    public void deleteContact(UUID id) {
        EmergencyContact contact = getContactById(id);
        emergencyContactRepository.delete(contact);
    }

    @Override
    public List<EmergencyContact> getAllContactsByUser(UUID userId) {
        return emergencyContactRepository.findALlByUserId(userId);
    }

    @Override
    public EmergencyContact updateContact(UUID id, EmergencyContactDto contact) {
        EmergencyContact existingContact = getContactById(id);

        existingContact.setName(contact.getName());
        existingContact.setPhoneNo(contact.getPhoneNo());
        existingContact.setRelation(contact.getRelation());

        return emergencyContactRepository.save(existingContact);
    }
}
