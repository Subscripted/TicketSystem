package de.lorenz.ticketsystem.dto.request;

public record LoginRequest
        (
                String identifier,
                String password
        ) {
}
