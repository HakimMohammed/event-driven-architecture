package org.example.cqrs.core.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
public class CreateAccountCommand {
    @TargetAggregateIdentifier
    private String id;
    private double balance;
    private String currency;
}
