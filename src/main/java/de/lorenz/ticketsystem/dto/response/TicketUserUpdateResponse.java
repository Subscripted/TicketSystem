package de.lorenz.ticketsystem.dto.response;

import java.util.Map;

public record TicketUserUpdateResponse(long userId, Map<String, Object> data) {
}
