package de.lorenz.ticketsystem.dto.response;

public record TicketCreateResponse
        (
                String title,
                Long assignedUserId,
                int type,
                int status
        ) {
}
