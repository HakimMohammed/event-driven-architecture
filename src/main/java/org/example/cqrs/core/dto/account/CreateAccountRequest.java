package org.example.cqrs.core.dto.account;

public record CreateAccountRequest(double balance, String currency) {
}
