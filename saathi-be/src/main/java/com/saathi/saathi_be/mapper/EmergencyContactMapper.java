package com.saathi.saathi_be.mapper;

import com.saathi.saathi_be.domain.dto.response.EmergencyContactResponse;
import com.saathi.saathi_be.domain.entity.EmergencyContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmergencyContactMapper {

    @Mapping(target = "userId", source = "user.id")
    EmergencyContactResponse toResponse(EmergencyContact emergencyContact);
}
