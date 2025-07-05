package com.saathi.saathi_be.repository;

import com.saathi.saathi_be.domain.entity.EmergencyContact;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, UUID> {
    boolean existsByPhoneNoAndUserId(String phoneNo, UUID userId);
    long countByUserId(UUID userId);
    List<EmergencyContact> findALlByUserId(UUID userId);
}
