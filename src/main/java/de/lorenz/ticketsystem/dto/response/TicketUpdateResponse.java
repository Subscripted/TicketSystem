package de.lorenz.ticketsystem.dto.response;

import java.util.Map;

public record TicketUpdateResponse
        (
                Map<String, Map<String, Object>> changedFields
        ) {
}

