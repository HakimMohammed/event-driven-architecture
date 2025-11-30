package org.example.cqrs.command;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.example.cqrs.core.commands.CreateAccountCommand;
import org.example.cqrs.core.commands.CreditAccountCommand;
import org.example.cqrs.core.dto.account.CreateAccountRequest;
import org.example.cqrs.core.dto.account.CreditAccountRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/command/account")
public class AccountCommandController {

    private CommandGateway commandGateway;

    public AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequest request) {
        return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(), request.balance(), request.currency()
        ));
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequest request) {
        return commandGateway.send(new CreditAccountCommand(
                request.id(), request.amount(), request.currency()
        ));
    }
}
