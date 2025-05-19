package com.bank.banksystem.mapper;

import com.bank.banksystem.dto.AccountDTO;
import com.bank.banksystem.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDTO dto);
    AccountDTO toDto(Account entity);
}