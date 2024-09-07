package org.example.exceptions;

public class GymIllegalPasswordException extends GymCustomRuntimeException {
    public GymIllegalPasswordException(String message) {
        super("Illegal password: " + message);
    }
}
