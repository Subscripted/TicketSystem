package de.lorenz.ticketsystem.dto.request;

import java.util.List;

public record TicketUpdateRequest
        (
                String title,
                String notiz,
                Integer status,
                Integer type,
                Long testerId,
                Long assignedUserId,
                String lang
        ) {
}
