package de.lorenz.ticketsystem.dto.request;

public record CreateLoginRequest
        (
                String email,
                String password
        ) {
}
