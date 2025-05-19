package com.bank.banksystem.controller;

import com.bank.banksystem.dto.AccountDTO;
import com.bank.banksystem.security.JwtUtil;
import com.bank.banksystem.service.AccountService;
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

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountController.class)
@Import({AccountControllerTest.MockConfig.class, AccountControllerTest.NoSecurityConfig.class})
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService accountService;

    private AccountDTO sampleDto;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        accountId = UUID.fromString("0b4e9535-3de9-40c1-90d7-1f943b4c081e");
        sampleDto = AccountDTO.builder()
                .id(accountId)
                .accountNumber("ACC123456")
                .accountType("SAVINGS")
                .balance(BigDecimal.valueOf(1000))
                .customerId(UUID.randomUUID())
                .build();
    }

    @Test
    void shouldCreateAccount() throws Exception {
        when(accountService.createAccount(any())).thenReturn(sampleDto);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"));
    }

    @Test
    void shouldGetAccountById() throws Exception {
        when(accountService.getAccount(accountId)).thenReturn(sampleDto);

        mockMvc.perform(get("/api/v1/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));
    }

    @Test
    void shouldUpdateAccount() throws Exception {
        when(accountService.updateAccount(eq(accountId), any())).thenReturn(sampleDto);

        mockMvc.perform(put("/api/v1/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount(accountId);

        mockMvc.perform(delete("/api/v1/accounts/{id}", accountId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAllAccounts() throws Exception {
        when(accountService.getAllAccounts(any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountNumber").value("ACC123456"));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public AccountService accountService() {
            return mock(AccountService.class);
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