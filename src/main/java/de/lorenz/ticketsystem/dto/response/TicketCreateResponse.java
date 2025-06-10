package de.lorenz.ticketsystem.dto.response;

public record TicketCreateResponse
        (
                String title,
                String notiz,
                Long assignedUserId,
                Integer type,
                Integer status,
                Long assignedTesterId
        ) {
}
