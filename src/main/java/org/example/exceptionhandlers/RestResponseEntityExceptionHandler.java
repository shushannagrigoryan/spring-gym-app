package org.example.exceptionhandlers;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.GymAuthenticationException;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.exceptions.GymIllegalIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = {GymIllegalArgumentException.class, GymEntityNotFoundException.class,
                               GymIllegalIdException.class})
    protected ResponseEntity<ExceptionResponse> handleIllegalArgumentException(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), status);
        log.debug("Response: {}", response);
        log.debug("Status Code: {}", status);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {GymAuthenticationException.class})
    protected ResponseEntity<ExceptionResponse> handleAuthenticationFailException(
            GymAuthenticationException e, HttpServletResponse servletResponse) throws IOException {
        System.out.println("Exception handling for authentication.");
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionResponse response = new ExceptionResponse(
                "Authentication error:" + e.getMessage(), status);
        log.debug("Response: {}", response);
        log.debug("Status Code: {}", status);
        servletResponse.getWriter().write(401);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {DateTimeParseException.class})
    protected ResponseEntity<ExceptionResponse> handleDateFormatException() {
        System.out.println("Exception handling for date format parse exception.");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse(
                "Wrong date format.Correct format is: yyyy-MM-dd", status);
        log.debug("Response: {}", response);
        log.debug("Status Code: {}", status);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    protected ResponseEntity<ExceptionResponse> handleNotSupportedMethodException(
            HttpRequestMethodNotSupportedException e) {
        System.out.println("Exception handling not supported request methods exception.");
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), status);
        log.debug("Response: {}", response);
        log.debug("Status Code: {}", status);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    protected ResponseEntity<ExceptionResponse> handleRequestNoHandlerFoundException(NoHandlerFoundException e) {
        System.out.println("Exception handling no handler found exception.");
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), status);
        log.debug("Response: {}", response);
        log.debug("Status Code: {}", status);
        return new ResponseEntity<>(response, status);
    }

}
