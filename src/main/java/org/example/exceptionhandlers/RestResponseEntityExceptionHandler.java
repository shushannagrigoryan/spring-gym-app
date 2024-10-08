package org.example.exceptionhandlers;

import org.example.exceptions.GymIllegalArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = {GymIllegalArgumentException.class})
    protected ResponseEntity<ExceptionResponse> handleIllegalArgumentException(GymIllegalArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), status);
        return new ResponseEntity<>(response, status);
    }
}