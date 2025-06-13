package de.lorenz.ticketsystem.dto.request;

public record TicketTimeSaveRequest
        (
                Long id,
                Long ticketId,
                Integer zeitInSekunden
        ) {
}
