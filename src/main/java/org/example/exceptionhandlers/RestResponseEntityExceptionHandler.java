package org.example.exceptionhandlers;

import org.example.exceptions.GymAuthenticationException;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = {GymIllegalArgumentException.class, GymEntityNotFoundException.class})
    protected ResponseEntity<ExceptionResponse> handleIllegalArgumentException(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), status);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {GymAuthenticationException.class})
    protected ResponseEntity<ExceptionResponse> handleAuthenticationFailException(GymAuthenticationException e) {
        System.out.println("Excepiton handling for authorization.");
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionResponse response = new ExceptionResponse(
                "Authentication error:" + e.getMessage(), status);
        return new ResponseEntity<>(response, status);
    }
}