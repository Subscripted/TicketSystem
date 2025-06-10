package de.lorenz.ticketsystem.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class MalformedJsonException extends RuntimeException {

    public MalformedJsonException(String s) {
        super(s);
    }
}

