package de.lorenz.ticketsystem.dto.response;

public record TicketCreateResponse
        (
                String title,
                Long assignedUserId,
                Integer type,
                Integer status,
                Long assignedTesterId
        ) {
}
