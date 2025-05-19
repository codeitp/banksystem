package com.bank.banksystem.service;

import com.bank.banksystem.dto.CustomerDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO dto);
    CustomerDTO updateCustomer(UUID id, CustomerDTO dto);
    CustomerDTO getCustomer(UUID id);
    List<CustomerDTO> getAllCustomers(String name, String startDate, String endDate, int page, int size);
    void deleteCustomer(UUID id);
}