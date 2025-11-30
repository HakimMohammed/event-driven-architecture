package org.example.cqrs.core.events;

import org.example.cqrs.core.enums.AccountStatus;

public record AccountCreatedEvent(String id, double initialBalance, String currency, AccountStatus status) {
}
