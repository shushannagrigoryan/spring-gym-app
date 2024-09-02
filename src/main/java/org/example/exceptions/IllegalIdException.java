package org.example.exceptions;

public class IllegalIdException extends CustomRuntimeException{
    public IllegalIdException(Long message) {
        super("Illegal id: " + message);
    }
}
