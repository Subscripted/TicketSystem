package de.lorenz.ticketsystem.dto.request;

public record TicketTimeSaveRequest
        (
                Long userId,
                Long ticketId,
                Integer zeitInSekunden,
                String lang
        ) {
}
