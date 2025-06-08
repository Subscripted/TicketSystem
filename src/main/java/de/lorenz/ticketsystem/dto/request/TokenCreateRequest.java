package de.lorenz.ticketsystem.dto.request;

public record TokenCreateRequest
        (
                String email,
                String clientId,
                String clientSecret
        ) {
}
