package org.example.exceptionhandlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.GymAuthenticationException;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.exceptions.GymIllegalIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

    /**
     * Exception handler for IllegalArgumentException.
     */
    @ExceptionHandler(value = {GymIllegalArgumentException.class, GymIllegalIdException.class})
    public ResponseEntity<ExceptionResponse<String>> handleIllegalArgumentException(
        HttpServletRequest request) {
        log.debug("Exception handling for bad request.");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse<String> response = new ExceptionResponse<>(
            "Illegal arguments", request.getRequestURI());
        return new ResponseEntity<>(response, status);
    }

    /**
     * Exception handler for GymEntityNotFoundException.
     */
    @ExceptionHandler(value = {GymEntityNotFoundException.class})
    public ResponseEntity<ExceptionResponse<String>> handleEntityNotFoundException(
        HttpServletRequest request) {
        log.debug("Exception handling for bad request for EntityNotFoundException.");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse<String> response = new ExceptionResponse<>(
            "Entity not found.", request.getRequestURI());
        return new ResponseEntity<>(response, status);
    }

    /**
     * Exception handler for GymAuthenticationException.
     */
    @ExceptionHandler(value = {GymAuthenticationException.class})
    public ResponseEntity<ExceptionResponse<String>> handleAuthenticationFailException(
        HttpServletRequest request,
        HttpServletResponse servletResponse) throws IOException {
        log.debug("Exception handling for authentication.");
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionResponse<String> response = new ExceptionResponse<>(
            "Authentication error: Bad credentials", request.getRequestURI());
        servletResponse.getWriter().write(401);
        return new ResponseEntity<>(response, status);
    }

    /**
     * Exception handler for DateTimeParseException.
     */
    @ExceptionHandler(value = {DateTimeParseException.class})
    public ResponseEntity<ExceptionResponse<String>> handleDateFormatException(HttpServletRequest request) {
        log.debug("Exception handling for date format parse exception.");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse<String> response = new ExceptionResponse<>(
            "Wrong date format.Correct format is: yyyy-MM-dd", request.getRequestURI());
        return new ResponseEntity<>(response, status);
    }

    /**
     * Exception handler for HttpRequestMethodNotSupportedException.
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ExceptionResponse<String>> handleNotSupportedMethodException(
        HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.debug("Exception handling not supported request methods exception.");
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ExceptionResponse<String> response = new ExceptionResponse<>(e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, status);
    }

    /**
     * Exception handler for NoHandlerFoundException.
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity<ExceptionResponse<String>> handleRequestNoHandlerFoundException(
        NoHandlerFoundException e, HttpServletRequest request) {
        log.debug("Exception handling no handler found exception.");
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionResponse<String> response = new ExceptionResponse<>(e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, status);
    }

    /**
     * Exception handler for HttpMessageNotReadableException.
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse<String>> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e, HttpServletRequest request) {
        log.debug("Exception handling for http message not readable exception.");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (e.getRootCause() instanceof DateTimeParseException) {
            return new ResponseEntity<>(new ExceptionResponse<>(
                "Wrong date format. Correct format is: yyyy-MM-dd'T'HH:mm:ss", request.getRequestURI()), status);
        }

        ExceptionResponse<String> response = new ExceptionResponse<>("Request body is missing or is invalid",
            request.getRequestURI());
        return new ResponseEntity<>(response, status);
    }

    /**
     * Exception handler for MethodArgumentNotValidException.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse<Map<String, String>>> handleInvalidRequestExceptions(
        MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.debug("Exception handling for MethodArgumentNotValidException");
        Map<String, String> response = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(e ->
            response.put(((FieldError) e).getField(), e.getDefaultMessage()));
        return new ResponseEntity<>(
            new ExceptionResponse<>(response,
                request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    /**
     * General exception handler.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse<String>> handleGeneralException(Exception e, HttpServletRequest request) {
        log.debug("Exception handling for general exceptions.");
        HttpStatus status = HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.debug(e.getMessage());
        ExceptionResponse<String> response = new ExceptionResponse<>("INTERNAL_SERVER_ERROR", request.getRequestURI());
        return new ResponseEntity<>(response, status);
    }

}
