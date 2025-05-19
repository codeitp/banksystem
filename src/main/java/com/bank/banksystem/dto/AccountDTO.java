package com.bank.banksystem.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for Account data transfer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private UUID id;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private UUID customerId;
}
