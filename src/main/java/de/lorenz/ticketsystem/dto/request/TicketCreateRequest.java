package de.lorenz.ticketsystem.dto.request;

public record TicketCreateRequest
        (
                String title,
                int type,
                int status,
                long assignedUserId
        ) {
}
