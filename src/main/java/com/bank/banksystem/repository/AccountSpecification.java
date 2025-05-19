package com.bank.banksystem.repository;

import com.bank.banksystem.entity.Account;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class AccountSpecification {

    public static Specification<Account> accountTypeEquals(String accountType) {
        return (root, query, builder) -> builder.equal(
                builder.lower(root.get("accountType")),
                accountType.toLowerCase()
        );
    }

    public static Specification<Account> belongsToCustomer(UUID customerId) {
        return (root, query, builder) -> builder.equal(root.get("customer").get("id"), customerId);
    }
}
