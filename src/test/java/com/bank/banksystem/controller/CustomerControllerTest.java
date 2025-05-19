package com.bank.banksystem.controller;

import com.bank.banksystem.dto.CustomerDTO;
import com.bank.banksystem.security.JwtUtil;
import com.bank.banksystem.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
@Import({CustomerControllerTest.MockConfig.class, CustomerControllerTest.NoSecurityConfig.class})
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerService customerService;

    private CustomerDTO sampleDto;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        sampleDto = CustomerDTO.builder()
                .id(customerId)
                .firstName("John")
                .lastName("Doe")
                .otherName("Smith")
                .build();
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        when(customerService.createCustomer(any())).thenReturn(sampleDto);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        when(customerService.getCustomer(eq(customerId))).thenReturn(sampleDto);

        mockMvc.perform(get("/api/v1/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        when(customerService.updateCustomer(eq(customerId), any())).thenReturn(sampleDto);

        mockMvc.perform(put("/api/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(customerId);

        mockMvc.perform(delete("/api/v1/customers/{id}", customerId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public CustomerService customerService() {
            return mock(CustomerService.class);
        }
    }

    // Disabled the security filters for speed and simple test
    @TestConfiguration
    static class NoSecurityConfig {

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring().requestMatchers("/**");
        }

        @Bean
        public JwtUtil jwtUtil() {
            return mock(JwtUtil.class);
        }
    }

}
