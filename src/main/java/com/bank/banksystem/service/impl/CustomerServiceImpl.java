package com.bank.banksystem.service.impl;

import com.bank.banksystem.dto.CustomerDTO;
import com.bank.banksystem.entity.Customer;
import com.bank.banksystem.exception.ResourceNotFoundException;
import com.bank.banksystem.mapper.CustomerMapper;
import com.bank.banksystem.repository.CustomerRepository;
import com.bank.banksystem.repository.CustomerSpecification;
import com.bank.banksystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer customer = mapper.toEntity(dto);
        return mapper.toDto(repository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(UUID id, CustomerDTO dto) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setOtherName(dto.getOtherName());
        return mapper.toDto(repository.save(customer));
    }

    @Override
    public CustomerDTO getCustomer(UUID id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found")));
    }

    @Override
    public List<CustomerDTO> getAllCustomers(String name, String startDate, String endDate, int page, int size) {
        Specification<Customer> spec = Specification.where(null);

        if (name != null && !name.isBlank()) {
            spec = spec.and(CustomerSpecification.nameContains(name));
        }

        if (startDate != null || endDate != null) {
            LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
            LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;
            spec = spec.and(CustomerSpecification.createdBetween(start, end));
        }

        return repository.findAll(spec, PageRequest.of(page, size))
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public void deleteCustomer(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found");
        }
        repository.deleteById(id);
    }
}
