package com.kaizenflow.bookquik.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.kaizenflow.bookquik.booking.domain.entity.Customer;
import com.kaizenflow.bookquik.booking.domain.request.CreateCustomerRequest;
import com.kaizenflow.bookquik.booking.domain.response.CustomerResponse;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    Customer requestToEntity(CreateCustomerRequest request);

    CustomerResponse entityToResponse(Customer customer);
}
