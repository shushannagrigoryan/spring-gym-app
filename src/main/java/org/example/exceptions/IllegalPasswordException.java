package org.example.exceptions;

public class IllegalPasswordException extends CustomRuntimeException{
    public IllegalPasswordException(String message) {
        super("Illegal password: " + message);
    }
}
