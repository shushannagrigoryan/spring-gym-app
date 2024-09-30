package org.example.exceptions;

// Every time this exception gets thrown, the same message should be
// copied multiple times as an argument, which may be inconvenient.
// I would pass the id to the constructor instead of the message,
// and call the super constructor with a custom message.

public class GymIllegalIdException extends GymCustomRuntimeException {
    public GymIllegalIdException(String message) {
        super(message);
    }

}
