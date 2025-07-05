package com.saathi.saathi_be.mapper;

import com.saathi.saathi_be.domain.dto.response.AuthUserResponse;
import com.saathi.saathi_be.domain.entity.EmergencyContact;
import com.saathi.saathi_be.domain.entity.Testimonial;
import com.saathi.saathi_be.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "totalEmergencyContacts", source = "emergencyContacts", qualifiedByName = "calculateContactCount")
    @Mapping(target = "totalTestimonials", source = "testimonials", qualifiedByName = "calculateTestimonialCount")
    AuthUserResponse toResponse(User user);

    @Named("calculateContactCount")
    default long calculateTotalEmergencyContacts(List<EmergencyContact> emergencyContacts) {
        return emergencyContacts == null ? 0 : emergencyContacts.size();
    }

    @Named("calculateTestimonialCount")
    default long calculateTotalTestimonials(List<Testimonial> testimonials) {
        return testimonials == null ? 0 : testimonials.size();
    }
}
