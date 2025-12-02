package org.example.cqrs.core.events;

public record AccountDebitedEvent(String id, double balance) {
}
