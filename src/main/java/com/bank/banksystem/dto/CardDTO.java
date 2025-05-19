package com.bank.banksystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    private UUID id;
    private String cardNumber;
    private String cardType;
    private LocalDate expiryDate;
    private UUID accountId;
}
