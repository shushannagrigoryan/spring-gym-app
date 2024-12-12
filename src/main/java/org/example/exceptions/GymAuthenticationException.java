package org.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class GymAuthenticationException extends AuthenticationException {
    public GymAuthenticationException(String message) {
        super(message);
    }
}
