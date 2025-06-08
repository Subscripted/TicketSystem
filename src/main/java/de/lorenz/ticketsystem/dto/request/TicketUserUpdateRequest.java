package de.lorenz.ticketsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public record TicketUserUpdateRequest
        (
                String email,
                String name,
                String shortName
        ){
}
