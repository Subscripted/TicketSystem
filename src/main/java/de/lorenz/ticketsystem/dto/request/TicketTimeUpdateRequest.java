package de.lorenz.ticketsystem.dto.request;

public record TicketTimeUpdateRequest
        (
                Long insertId,
                Integer zeit
        ) {
}
