package de.lorenz.ticketsystem.dto.request;

import java.util.List;

public record TicketUpdateRequest
        (
                String title,
                Integer status,
                Integer type,
                Long testerId,
                Long assignedUserId
        ) {
}
