package org.example.cqrs.command;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.cqrs.core.commands.ActivateAccountCommand;
import org.example.cqrs.core.commands.CreateAccountCommand;
import org.example.cqrs.core.commands.CreditAccountCommand;
import org.example.cqrs.core.enums.AccountStatus;
import org.example.cqrs.core.enums.Currency;
import org.example.cqrs.core.events.AccountActivatedEvent;
import org.example.cqrs.core.events.AccountCreatedEvent;
import org.example.cqrs.core.events.AccountCreditedEvent;
import org.example.cqrs.core.services.CurrencyExchangeService;
import org.example.cqrs.core.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Aggregate
@Getter
@Setter
public class AccountAggregate {
    @AggregateIdentifier
    private String id;
    private double balance;
    private AccountStatus status;
    private Currency currency;

    @Autowired
    private CurrencyExchangeService exchangeService;

    // Axon constructor
    public AccountAggregate() {
    }

    // CREATION
    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        log.info("------------------------- Account Command Received -----------------------");
        if (command.balance() < 0)
            throw new IllegalArgumentException("Balance cannot be negative");

        AggregateLifecycle.apply(
                new AccountCreatedEvent(
                        command.id(),
                        command.balance(),
                        command.currency(),
                        AccountStatus.CREATED
                ));
    }

    @EventSourcingHandler
    public void onCreation(AccountCreatedEvent event) {
        log.info("------------------------- Account Event Received -----------------------");
        this.setId(event.id());
        this.setBalance(event.balance());
        this.setCurrency(event.currency());
        this.setStatus(event.status());
    }

    // ACTIVATION
    @CommandHandler
    public void activateAccount(ActivateAccountCommand command) {
        log.info("------------------------- Activate Command Received -----------------------");
        if (this.getStatus().equals(AccountStatus.ACTIVATED))
            throw new IllegalArgumentException("Account is already activated");
        if (ObjectUtils.equalsAny(status, AccountStatus.BLOCKED, AccountStatus.SUSPENDED))
            throw new IllegalArgumentException("Account cannot be activated");

        AggregateLifecycle.apply(new AccountActivatedEvent(command.id()));
    }

    @EventSourcingHandler
    public void onActivation(AccountActivatedEvent event) {
        log.info("------------------------- Activate Event Received -----------------------");
        this.status = AccountStatus.ACTIVATED;
    }

    // CREDIT
    @CommandHandler
    public AccountAggregate(CreditAccountCommand command) {
        log.info("------------------------- Credit Command Received -----------------------");
        if (!this.getStatus().equals(AccountStatus.ACTIVATED))
            throw new IllegalArgumentException("Account must be activated to make transactions");

        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.id(),
                exchangeService.convert(command.amount(), command.currency(), currency)
        ));
    }

    @EventSourcingHandler
    public void onCredit(AccountCreditedEvent event) {
        log.info("------------------------- Credit Event Received -----------------------");
        this.balance += event.balance();
    }
}
