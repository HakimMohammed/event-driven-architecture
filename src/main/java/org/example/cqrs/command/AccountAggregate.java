package org.example.cqrs.command;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.cqrs.core.commands.CreateAccountCommand;
import org.example.cqrs.core.enums.AccountStatus;
import org.example.cqrs.core.events.AccountCreatedEvent;

@Slf4j
@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String id;
    private double balance;
    private AccountStatus status;
    private String currency;

    // Axon constructor
    public AccountAggregate() {
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        log.info("------------------------- Account Command Received -----------------------");
        if (command.getBalance() < 0)
            throw new IllegalArgumentException("Balance cannot be negative");

        AggregateLifecycle.apply(
            new AccountCreatedEvent(
                command.getId(),
                command.getBalance(),
                command.getCurrency(),
                AccountStatus.CREATED
        ));
    }

    @EventSourcingHandler
    public void onCreation(AccountCreatedEvent event) {
        log.info("------------------------- Account Event Received -----------------------");
        this.id = event.id();
        this.balance = event.initialBalance();
        this.status = event.status();
        this.currency = event.currency();
    }
}
