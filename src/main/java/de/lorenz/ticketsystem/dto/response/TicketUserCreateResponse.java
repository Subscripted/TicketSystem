package de.lorenz.ticketsystem.dto.response;

import java.time.LocalDateTime;

public record TicketUserCreateResponse
        (
                String email,
                String name,
                LocalDateTime at
        ) {
}
