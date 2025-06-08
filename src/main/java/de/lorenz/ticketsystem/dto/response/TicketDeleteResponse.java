package de.lorenz.ticketsystem.dto.response;

public record TicketDeleteResponse
        (
                Long id,
                String title,
                long assignedUserId
        ) {
}
