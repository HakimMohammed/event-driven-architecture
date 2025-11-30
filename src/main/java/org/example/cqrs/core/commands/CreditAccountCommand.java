package org.example.cqrs.core.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.example.cqrs.core.enums.Currency;

public record CreditAccountCommand(
        @TargetAggregateIdentifier String id,
        double amount,
        Currency currency
) {}
