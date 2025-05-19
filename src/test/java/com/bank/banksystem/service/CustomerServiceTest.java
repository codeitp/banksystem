package com.bank.banksystem.service;

import com.bank.banksystem.dto.CustomerDTO;
import com.bank.banksystem.entity.Customer;
import com.bank.banksystem.mapper.CustomerMapper;
import com.bank.banksystem.repository.CustomerRepository;
import com.bank.banksystem.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerMapper mapper;

    @InjectMocks
    private CustomerServiceImpl service;

    private Customer customer;
    private CustomerDTO customerDTO;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();

        customer = Customer.builder()
                .id(customerId)
                .firstName("John")
                .lastName("Doe")
                .otherName("Kip")
                .build();

        customerDTO = CustomerDTO.builder()
                .id(customerId)
                .firstName("John")
                .lastName("Doe")
                .otherName("Kip")
                .build();
    }

    @Test
    void testCreateCustomer() {
        when(mapper.toEntity(any(CustomerDTO.class))).thenReturn(customer);
        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(mapper.toDto(any(Customer.class))).thenReturn(customerDTO);

        CustomerDTO result = service.createCustomer(customerDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(repository, times(1)).save(customer);
    }

    @Test
    void testGetCustomerById() {
        when(repository.findById(customerId)).thenReturn(Optional.of(customer));
        when(mapper.toDto(customer)).thenReturn(customerDTO);

        CustomerDTO result = service.getCustomer(customerId);

        assertEquals(customerId, result.getId());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testUpdateCustomer() {
        CustomerDTO updatedDTO = CustomerDTO.builder()
                .id(customerId)
                .firstName("Jane")
                .lastName("Smith")
                .otherName("Mutua")
                .build();

        Customer updatedEntity = Customer.builder()
                .id(customerId)
                .firstName("Jane")
                .lastName("Smith")
                .otherName("Mutua")
                .build();

        when(repository.findById(customerId)).thenReturn(Optional.of(customer));
        when(repository.save(any(Customer.class))).thenReturn(updatedEntity);
        when(mapper.toDto(any(Customer.class))).thenReturn(updatedDTO);

        CustomerDTO result = service.updateCustomer(customerId, updatedDTO);

        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers);

        // Mock the Specification version of findAll
        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(customerPage);
        when(mapper.toDto(any(Customer.class)))
                .thenReturn(customerDTO);

        List<CustomerDTO> result = service.getAllCustomers(null, null, null, 0, 10);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void testDeleteCustomer() {
        when(repository.existsById(customerId)).thenReturn(true);
        doNothing().when(repository).deleteById(customerId);

        assertDoesNotThrow(() -> service.deleteCustomer(customerId));

        verify(repository, times(1)).deleteById(customerId);
    }
}