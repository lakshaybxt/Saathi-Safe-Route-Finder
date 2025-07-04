package com.saathi.saathi_be.mapper;

import com.saathi.saathi_be.domain.dto.response.TestimonialResponse;
import com.saathi.saathi_be.domain.entity.Testimonial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestimonialMapper {

    @Mapping(target = "city", source = "place.locality")
    TestimonialResponse toResponse(Testimonial testimonial);
}
