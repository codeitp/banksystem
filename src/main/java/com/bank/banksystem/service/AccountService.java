package com.bank.banksystem.service;

import com.bank.banksystem.dto.AccountDTO;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountDTO createAccount(AccountDTO dto);
    AccountDTO updateAccount(UUID id, AccountDTO dto);
    AccountDTO getAccount(UUID id);
    List<AccountDTO> getAllAccounts(String accountType, UUID customerId, int page, int size);
    void deleteAccount(UUID id);
}
