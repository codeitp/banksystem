package com.bank.banksystem.service.impl;

import com.bank.banksystem.dto.AccountDTO;
import com.bank.banksystem.entity.Account;
import com.bank.banksystem.exception.ResourceNotFoundException;
import com.bank.banksystem.mapper.AccountMapper;
import com.bank.banksystem.repository.AccountRepository;
import com.bank.banksystem.repository.AccountSpecification;
import com.bank.banksystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountDTO createAccount(AccountDTO dto) {
        Account account = accountMapper.toEntity(dto);
        return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    public AccountDTO updateAccount(UUID id, AccountDTO dto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setAccountNumber(dto.getAccountNumber());
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());
        account.setCustomerId(dto.getCustomerId());

        return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    public AccountDTO getAccount(UUID id) {
        return accountMapper.toDto(accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found")));
    }

    @Override
    public List<AccountDTO> getAllAccounts(String accountType, UUID customerId, int page, int size) {
        Specification<Account> spec = Specification.where(null);

        if (accountType != null && !accountType.isBlank()) {
            spec = spec.and(AccountSpecification.accountTypeEquals(accountType));
        }

        if (customerId != null) {
            spec = spec.and(AccountSpecification.belongsToCustomer(customerId));
        }

        return accountRepository.findAll(spec, PageRequest.of(page, size))
                .map(accountMapper::toDto)
                .toList();
    }

    @Override
    public void deleteAccount(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found");
        }
        accountRepository.deleteById(id);
    }
}
