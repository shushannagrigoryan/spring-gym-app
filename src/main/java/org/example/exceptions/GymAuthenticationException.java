package org.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class GymAuthenticationException extends RuntimeException {
    public GymAuthenticationException(String message) {
        super(message);
    }
}
