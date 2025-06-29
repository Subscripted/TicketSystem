package de.lorenz.ticketsystem.dto.response;

public record TicketSelectResponse(
        Long id,
        Integer status,
        Integer type,
        Long assignedUserId,
        Long assignedTesterId,
        String title,
        String notiz
) {
}
