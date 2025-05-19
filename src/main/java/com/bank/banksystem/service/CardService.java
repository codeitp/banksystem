package com.bank.banksystem.service;

import com.bank.banksystem.dto.CardDTO;

import java.util.List;
import java.util.UUID;

public interface CardService {
    CardDTO createCard(CardDTO dto);
    CardDTO updateCard(UUID id, CardDTO dto);
    CardDTO getCard(UUID id);
    List<CardDTO> getAllCards(int page, int size);
    void deleteCard(UUID id);
}

