package org.example.cqrs.core.dto.account;

import org.example.cqrs.core.enums.Currency;

public record CreditAccountRequest(String id, double amount, Currency currency) {
}
