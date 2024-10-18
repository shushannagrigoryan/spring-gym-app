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
    //@Schema(description = "Error message")
    private String message;
    //@Schema(description = "HTTP status code", example = "400 BAD REQUEST")
    private HttpStatus httpStatus;
}
