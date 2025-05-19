package com.bank.banksystem.mapper;

import com.bank.banksystem.dto.CardDTO;
import com.bank.banksystem.entity.Card;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public Card toEntity(CardDTO dto) {
        return Card.builder()
                .id(dto.getId())
                .cardNumber(dto.getCardNumber())
                .cardType(dto.getCardType())
                .expiryDate(dto.getExpiryDate())
                .accountId(dto.getAccountId())
                .build();
    }

    public CardDTO toDto(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .cardType(card.getCardType())
                .expiryDate(card.getExpiryDate())
                .accountId(card.getAccountId())
                .build();
    }
}
