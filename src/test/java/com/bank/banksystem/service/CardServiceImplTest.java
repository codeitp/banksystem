package com.bank.banksystem.service;

import com.bank.banksystem.dto.CardDTO;
import com.bank.banksystem.entity.Card;
import com.bank.banksystem.exception.ResourceNotFoundException;
import com.bank.banksystem.mapper.CardMapper;
import com.bank.banksystem.repository.CardRepository;
import com.bank.banksystem.service.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private UUID cardId;
    private CardDTO sampleDto;
    private Card sampleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cardId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        sampleDto = CardDTO.builder()
                .id(cardId)
                .cardNumber("CARD123456")
                .cardType("VISA")
                .expiryDate(LocalDate.now().plusYears(3))
                .accountId(accountId)
                .build();

        sampleEntity = Card.builder()
                .id(cardId)
                .cardNumber("CARD123456")
                .cardType("VISA")
                .expiryDate(sampleDto.getExpiryDate())
                .accountId(accountId)
                .build();
    }

    @Test
    void shouldCreateCard() {
        when(cardMapper.toEntity(sampleDto)).thenReturn(sampleEntity);
        when(cardRepository.save(sampleEntity)).thenReturn(sampleEntity);
        when(cardMapper.toDto(sampleEntity)).thenReturn(sampleDto);

        CardDTO result = cardService.createCard(sampleDto);

        assertNotNull(result);
        assertEquals("CARD123456", result.getCardNumber());
        verify(cardRepository).save(sampleEntity);
    }

    @Test
    void shouldGetCardById() {
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(sampleEntity));
        when(cardMapper.toDto(sampleEntity)).thenReturn(sampleDto);

        CardDTO result = cardService.getCard(cardId);

        assertEquals("VISA", result.getCardType());
    }

    @Test
    void shouldThrowIfCardNotFound() {
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cardService.getCard(cardId));
    }

    @Test
    void shouldUpdateCard() {
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(sampleEntity));
        when(cardRepository.save(any())).thenReturn(sampleEntity);
        when(cardMapper.toDto(any())).thenReturn(sampleDto);

        CardDTO result = cardService.updateCard(cardId, sampleDto);

        assertEquals("VISA", result.getCardType());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void shouldDeleteCard() {
        when(cardRepository.existsById(cardId)).thenReturn(true);
        doNothing().when(cardRepository).deleteById(cardId);

        cardService.deleteCard(cardId);

        verify(cardRepository).deleteById(cardId);
    }

    @Test
    void shouldThrowIfDeleteCardNotFound() {
        when(cardRepository.existsById(cardId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> cardService.deleteCard(cardId));
    }

    @Test
    void shouldGetAllCards() {
        when(cardRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(sampleEntity)));
        when(cardMapper.toDto(sampleEntity)).thenReturn(sampleDto);

        List<CardDTO> result = cardService.getAllCards(0, 10);

        assertThat(result).hasSize(1);
        verify(cardRepository).findAll(PageRequest.of(0, 10));
    }
}
