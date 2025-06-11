package de.lorenz.ticketsystem.dto.response;

public record TicketSelectResponse(
        Long id,
        Long assignedUserId,
        Long assignedTesterId,
        String title,
        String notiz
) {
}
