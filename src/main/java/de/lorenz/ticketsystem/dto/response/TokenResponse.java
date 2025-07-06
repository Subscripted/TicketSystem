package de.lorenz.ticketsystem.dto.response;

public record TokenResponse
        (
                String token,
                Integer expired_in
        ) {
}
