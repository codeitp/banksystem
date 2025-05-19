package com.bank.banksystem.service;

import com.bank.banksystem.dto.AccountDTO;
import com.bank.banksystem.entity.Account;
import com.bank.banksystem.exception.ResourceNotFoundException;
import com.bank.banksystem.mapper.AccountMapper;
import com.bank.banksystem.repository.AccountRepository;
import com.bank.banksystem.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountDTO sampleDto;
    private Account sampleEntity;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        accountId = UUID.randomUUID();

        sampleDto = AccountDTO.builder()
                .id(accountId)
                .accountNumber("ACC123456")
                .accountType("SAVINGS")
                .balance(BigDecimal.valueOf(1000))
                .customerId(UUID.randomUUID())
                .build();

        sampleEntity = Account.builder()
                .id(accountId)
                .accountNumber("ACC123456")
                .accountType("SAVINGS")
                .balance(BigDecimal.valueOf(1000))
                .customerId(sampleDto.getCustomerId())
                .build();
    }

    @Test
    void shouldCreateAccount() {
        when(accountMapper.toEntity(sampleDto)).thenReturn(sampleEntity);
        when(accountRepository.save(sampleEntity)).thenReturn(sampleEntity);
        when(accountMapper.toDto(sampleEntity)).thenReturn(sampleDto);

        AccountDTO result = accountService.createAccount(sampleDto);

        assertNotNull(result);
        assertEquals("ACC123456", result.getAccountNumber());
        verify(accountRepository, times(1)).save(sampleEntity);
    }

    @Test
    void shouldGetAccountById() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(sampleEntity));
        when(accountMapper.toDto(sampleEntity)).thenReturn(sampleDto);

        AccountDTO result = accountService.getAccount(accountId);

        assertEquals(sampleDto.getAccountNumber(), result.getAccountNumber());
        verify(accountRepository).findById(accountId);
    }

    @Test
    void shouldThrowIfAccountNotFound() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccount(accountId));
    }

    @Test
    void shouldUpdateAccount() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(sampleEntity));
        when(accountRepository.save(any())).thenReturn(sampleEntity);
        when(accountMapper.toDto(any())).thenReturn(sampleDto);

        AccountDTO result = accountService.updateAccount(accountId, sampleDto);

        assertEquals("SAVINGS", result.getAccountType());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldDeleteAccount() {
        when(accountRepository.existsById(accountId)).thenReturn(true);
        doNothing().when(accountRepository).deleteById(accountId);

        accountService.deleteAccount(accountId);

        verify(accountRepository).deleteById(accountId);
    }

    @Test
    void shouldThrowIfAccountToDeleteNotFound() {
        when(accountRepository.existsById(accountId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccount(accountId));
    }

    @Test
    void shouldReturnFilteredAccounts() {
        Page<Account> accountPage = new PageImpl<>(List.of(sampleEntity));

        when(accountRepository.findAll(
                ArgumentMatchers.<org.springframework.data.jpa.domain.Specification<Account>>any(),
                any(Pageable.class))
        ).thenReturn(accountPage);

        when(accountMapper.toDto(sampleEntity)).thenReturn(sampleDto);

        List<AccountDTO> result = accountService.getAllAccounts("SAVINGS", sampleDto.getCustomerId(), 0, 10);

        assertThat(result).hasSize(1);
        verify(accountRepository).findAll(
                ArgumentMatchers.<org.springframework.data.jpa.domain.Specification<com.bank.banksystem.entity.Account>>any(),
                any(Pageable.class)
        );
    }
}


