package de.lorenz.ticketsystem.dto.response;

import java.time.LocalDateTime;

public record TicketUserDeleteResponse

        (
                String name,
                String email,
                LocalDateTime delete_time
        ) {
}
