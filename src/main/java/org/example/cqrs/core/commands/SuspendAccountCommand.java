package org.example.cqrs.core.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record SuspendAccountCommand(@TargetAggregateIdentifier String id) {
}
