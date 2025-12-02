package org.example.cqrs.query.controllers;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.example.cqrs.core.entities.Account;
import org.example.cqrs.core.queries.GetAccountQuery;
import org.example.cqrs.core.queries.GetAllAccountsQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/account")
public class AccountQueryController {
    private QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public CompletableFuture<List<Account>> getAccounts() {
        return queryGateway.query(new GetAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class));
    }

    @GetMapping("/{id}")
    public CompletableFuture<Account> getAccount(@PathVariable String id) {
        return queryGateway.query(new GetAccountQuery(id), ResponseTypes.instanceOf(Account.class));
    }
}
