package com.saathi.saathi_be.mapper;

import com.saathi.saathi_be.domain.dto.PlaceDto;
import com.saathi.saathi_be.domain.entity.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlaceMapper {
    @Mapping(target = "locality", source = "locality")
    PlaceDto toDto(Place place);
//    Place toEntity(PlaceDto placeDto);
}
