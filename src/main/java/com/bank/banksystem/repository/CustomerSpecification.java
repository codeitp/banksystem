package com.bank.banksystem.repository;

import com.bank.banksystem.entity.Customer;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerSpecification {

    public static Specification<Customer> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            String likePattern = "%" + name.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), likePattern),
                    cb.like(cb.lower(root.get("lastName")), likePattern),
                    cb.like(cb.lower(root.get("otherName")), likePattern)
            );
        };
    }

    public static Specification<Customer> createdBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return null;
            if (start != null && end != null)
                return cb.between(root.get("createdAt"), start.atStartOfDay(), end.atTime(23, 59, 59));
            if (start != null)
                return cb.greaterThanOrEqualTo(root.get("createdAt"), start.atStartOfDay());
            return cb.lessThanOrEqualTo(root.get("createdAt"), end.atTime(23, 59, 59));
        };
    }
}
