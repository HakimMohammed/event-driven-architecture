package org.example.cqrs.command;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.example.cqrs.core.commands.*;
import org.example.cqrs.core.dto.account.CreateAccountRequest;
import org.example.cqrs.core.dto.account.CreditAccountRequest;
import org.example.cqrs.core.dto.account.DebitAccountRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/command/account")
public class AccountCommandController {

    private final CommandGateway commandGateway;

    public AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequest request) {
        return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(), request.balance(), request.currency()
        ));
    }

    @PostMapping("/{id}/activate")
    public CompletableFuture<String> activateAccount(@PathVariable String id) {
        return commandGateway.send(new ActivateAccountCommand(id));
    }

    @PostMapping("/{id}/suspend")
    public CompletableFuture<String> suspendAccount(@PathVariable String id) {
        return commandGateway.send(new SuspendAccountCommand(id));
    }

    @PostMapping("/{id}/block")
    public CompletableFuture<String> blockAccount(@PathVariable String id) {
        return commandGateway.send(new BlockAccountCommand(id));
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequest request) {
        return commandGateway.send(new CreditAccountCommand(
                request.id(), request.amount(), request.currency()
        ));
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequest request) {
        return commandGateway.send(new CreditAccountCommand(
                request.id(), request.amount(), request.currency()
        ));
    }

}
