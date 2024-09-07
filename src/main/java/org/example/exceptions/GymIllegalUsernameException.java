package org.example.exceptions;

public class GymIllegalUsernameException extends GymCustomRuntimeException {
    public GymIllegalUsernameException(String username) {
        super("Illegal username: " + username);
    }
}
