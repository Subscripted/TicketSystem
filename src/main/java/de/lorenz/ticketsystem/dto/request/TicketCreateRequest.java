package de.lorenz.ticketsystem.dto.request;

public record TicketCreateRequest
        (
                String title,
                Integer type,
                Integer status,
                Long assignedUserId,
                Long assignedTesterId
        ) {
}
