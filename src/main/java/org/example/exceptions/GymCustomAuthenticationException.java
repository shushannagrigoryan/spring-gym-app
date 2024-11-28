package org.example.exceptions;

import org.springframework.security.core.AuthenticationException;

public class GymCustomAuthenticationException extends AuthenticationException {
    public GymCustomAuthenticationException(String msg) {
        super(msg);
    }
}
