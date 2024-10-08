package org.example.exceptionhandlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ExceptionResponse {
    private String message;
    private HttpStatus httpStatus;
}
