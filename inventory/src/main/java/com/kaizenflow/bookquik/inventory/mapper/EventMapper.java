package com.kaizenflow.bookquik.inventory.mapper;

import com.kaizenflow.bookquik.inventory.domain.entity.Event;
import com.kaizenflow.bookquik.inventory.domain.response.EventInventoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface EventMapper {

    EventInventoryResponse eventInventoryResponse(Event event);
}
