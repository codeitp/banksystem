package com.bank.banksystem.service.impl;

import com.bank.banksystem.dto.CardDTO;
import com.bank.banksystem.entity.Card;
import com.bank.banksystem.exception.ResourceNotFoundException;
import com.bank.banksystem.mapper.CardMapper;
import com.bank.banksystem.repository.CardRepository;
import com.bank.banksystem.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public CardDTO createCard(CardDTO dto) {
        Card card = cardMapper.toEntity(dto);
        return cardMapper.toDto(cardRepository.save(card));
    }

    @Override
    public CardDTO updateCard(UUID id, CardDTO dto) {
        Card existing = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        existing.setCardNumber(dto.getCardNumber());
        existing.setCardType(dto.getCardType());
        existing.setExpiryDate(dto.getExpiryDate());
        existing.setAccountId(dto.getAccountId());

        return cardMapper.toDto(cardRepository.save(existing));
    }

    @Override
    public CardDTO getCard(UUID id) {
        return cardMapper.toDto(cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found")));
    }

    @Override
    public List<CardDTO> getAllCards(int page, int size) {
        return cardRepository.findAll(PageRequest.of(page, size))
                .map(cardMapper::toDto)
                .toList();
    }

    @Override
    public void deleteCard(UUID id) {
        if (!cardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Card not found");
        }
        cardRepository.deleteById(id);
    }
}
