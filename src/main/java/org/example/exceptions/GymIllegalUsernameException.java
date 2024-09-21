package org.example.exceptions;

public class GymIllegalUsernameException extends GymCustomRuntimeException {
    public GymIllegalUsernameException(String message) {
        super(message);
    }
}
