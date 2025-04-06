package com.kaizenflow.bookquik.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.kaizenflow.bookquik.inventory.domain.entity.Venue;
import com.kaizenflow.bookquik.inventory.domain.response.VenueInventoryResponse;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface VenueMapper {

    @Mapping(target = "venueName", source = "name")
    VenueInventoryResponse entityToResponse(Venue venue);
}
