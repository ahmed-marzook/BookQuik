package com.kaizenflow.bookquik.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.kaizenflow.bookquik.inventory.domain.entity.Event;
import com.kaizenflow.bookquik.inventory.domain.response.EventInventoryResponse;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
        uses = {VenueMapper.class})
public interface EventMapper {

    @Mapping(source = "id", target = "eventId")
    @Mapping(source = "venue", target = "venue")
    EventInventoryResponse entityToResponse(Event event);
}
