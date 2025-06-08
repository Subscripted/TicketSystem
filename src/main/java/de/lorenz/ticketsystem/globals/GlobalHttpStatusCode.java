package de.lorenz.ticketsystem.globals;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalHttpStatusCode {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    TO_MANY_REQUESTS(429),
    INTERNAL_ERROR(500);

    private final int code;
}
