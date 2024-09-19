package org.example.exceptions;

public class GymEntityNotFoundException extends GymCustomRuntimeException {
    public GymEntityNotFoundException(String message) {
        super(message);
    }
}
