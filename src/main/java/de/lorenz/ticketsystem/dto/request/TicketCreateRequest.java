package de.lorenz.ticketsystem.dto.request;

public record TicketCreateRequest
        (
                String title,
                String notiz,
                Integer type,
                Integer status,
                Long assignedUserId,
                Long assignedTesterId,
                String lang
        ) {
}
