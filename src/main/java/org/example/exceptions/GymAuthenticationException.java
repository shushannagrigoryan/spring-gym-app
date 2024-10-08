package org.example.exceptions;

public class GymAuthenticationException extends RuntimeException {
    public GymAuthenticationException(String message) {
        super(message);
    }
}
