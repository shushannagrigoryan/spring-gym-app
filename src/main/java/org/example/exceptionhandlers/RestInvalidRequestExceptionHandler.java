package org.example.exceptionhandlers;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestInvalidRequestExceptionHandler {

    /**
     * handle exception.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRequestExceptions(
            MethodArgumentNotValidException exception) {
        Map<String, String> response = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(e ->
                response.put(((FieldError) e).getField(), e.getDefaultMessage()));
        log.debug("response : {}", response);
        log.debug("Http Status: {}", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}