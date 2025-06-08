package de.lorenz.ticketsystem.dto.request;

public record TicketUpdateRequest
        (
                long id,
                int status
        ) {
}
