package de.lorenz.ticketsystem.dto.request;

public record TicketUserCreateRequest
        (
                String email,
                String name
        ) {
}
