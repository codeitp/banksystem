package com.bank.banksystem.mapper;

import com.bank.banksystem.dto.CustomerDTO;
import com.bank.banksystem.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO toDto(Customer customer);
    Customer toEntity(CustomerDTO dto);
}