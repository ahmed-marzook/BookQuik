package com.kaizenflow.bookquik.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.kaizenflow.bookquik.inventory.domain.entity.Event;
import com.kaizenflow.bookquik.inventory.domain.response.EventInventoryResponse;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface EventMapper {

    @Mapping(source = "id", target = "eventId")
    EventInventoryResponse entityToResponse(Event event);
}
