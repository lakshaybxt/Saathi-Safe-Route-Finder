package com.saathi.saathi_be.controller;

import com.saathi.saathi_be.domain.dto.EmergencyContactDto;
import com.saathi.saathi_be.domain.dto.response.EmergencyContactResponse;
import com.saathi.saathi_be.domain.entity.EmergencyContact;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.mapper.EmergencyContactMapper;
import com.saathi.saathi_be.service.EmergencyContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/emergency")
@RequiredArgsConstructor
public class EmergencyController {

    private final EmergencyContactService emergencyContactService;
    private final EmergencyContactMapper emergencyContactMapper;

    @PostMapping(path = "/contact-add")
    public ResponseEntity<EmergencyContactResponse> addContact(
            @Valid @RequestBody EmergencyContactDto contact,
            @RequestAttribute UUID userId
    ) {
        EmergencyContact emergencyContact = emergencyContactService.createContact(contact, userId);
        EmergencyContactResponse response = emergencyContactMapper.toResponse(emergencyContact);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(path = "/contact-delete/{id}")
    public ResponseEntity<?> deleteContact(
            @RequestAttribute UUID userId,
            @PathVariable UUID id
    ) {
        EmergencyContact existingContact = emergencyContactService.getContactById(id);

        if(!existingContact.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        emergencyContactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/contacts")
    public ResponseEntity<List<EmergencyContactResponse>> getAllContactsByUser(@RequestAttribute UUID userId) {
        List<EmergencyContact> contacts = emergencyContactService.getAllContactsByUser(userId);

        if(contacts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EmergencyContactResponse> responses = contacts.stream()
                .map(emergencyContactMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping(path = "/contact/{id}")
    public ResponseEntity<EmergencyContactResponse> getContact(
            @RequestAttribute UUID userId,
            @PathVariable UUID id
    ) {
        EmergencyContact contact = emergencyContactService.getContactById(id);

        if(!contact.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        EmergencyContactResponse response = emergencyContactMapper.toResponse(contact);

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/contact-update/{id}")
    public ResponseEntity<EmergencyContactResponse> updateContact(
            @RequestAttribute UUID userId,
            @PathVariable UUID id,
            @Valid @RequestBody EmergencyContactDto contact
    ) {
        EmergencyContact existingContact = emergencyContactService.getContactById(id);

        if(!existingContact.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        EmergencyContact updatedContact = emergencyContactService.updateContact(id, contact);
        EmergencyContactResponse response = emergencyContactMapper.toResponse(updatedContact);

        return ResponseEntity.ok(response);
    }
}
