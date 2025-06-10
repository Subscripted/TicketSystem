package de.lorenz.ticketsystem.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.StandardException;

@StandardException
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidJsonException extends Exception {

    String json;

}
