package com.bank.banksystem.controller;

import com.bank.banksystem.dto.CardDTO;
import com.bank.banksystem.security.JwtUtil;
import com.bank.banksystem.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CardController.class)
@Import({CardControllerTest.MockConfig.class, CardControllerTest.NoSecurityConfig.class})
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardService cardService;

    private UUID cardId;
    private CardDTO sampleDto;

    @BeforeEach
    void setUp() {
        cardId = UUID.randomUUID();
        sampleDto = CardDTO.builder()
                .id(cardId)
                .cardNumber("CARD123456")
                .cardType("VISA")
                .expiryDate(LocalDate.now().plusYears(2))
                .accountId(UUID.randomUUID())
                .build();
    }

    @Test
    void shouldCreateCard() throws Exception {
        when(cardService.createCard(any())).thenReturn(sampleDto);

        mockMvc.perform(post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("CARD123456"));
    }

    @Test
    void shouldGetCardById() throws Exception {
        when(cardService.getCard(eq(cardId))).thenReturn(sampleDto);

        mockMvc.perform(get("/api/v1/cards/{id}", cardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardType").value("VISA"));
    }

    @Test
    void shouldUpdateCard() throws Exception {
        when(cardService.updateCard(eq(cardId), any())).thenReturn(sampleDto);

        mockMvc.perform(put("/api/v1/cards/{id}", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("CARD123456"));
    }

    @Test
    void shouldDeleteCard() throws Exception {
        doNothing().when(cardService).deleteCard(cardId);

        mockMvc.perform(delete("/api/v1/cards/{id}", cardId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAllCards() throws Exception {
        when(cardService.getAllCards(anyInt(), anyInt())).thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/api/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cardNumber").value("CARD123456"));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public CardService cardService() {
            return mock(CardService.class);
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