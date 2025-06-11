package de.lorenz.ticketsystem.dto.request;

public record TicketSelectRequest(
        Long id,
        Long assignedUserId,
        Integer type,
        Integer status
) {
}
