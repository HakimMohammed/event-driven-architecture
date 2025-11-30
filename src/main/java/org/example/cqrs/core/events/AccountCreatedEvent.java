package org.example.cqrs.core.events;

import org.example.cqrs.core.enums.AccountStatus;
import org.example.cqrs.core.enums.Currency;

public record AccountCreatedEvent(String id, double initialBalance, Currency currency, AccountStatus status) {
}
