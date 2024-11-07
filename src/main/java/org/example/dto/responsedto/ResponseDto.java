package org.example.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T> {
    private T payload;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime timestamp;

    /**
     * Constructs a responseDto object.
     */
    public ResponseDto(T payload, String message) {
        this.payload = payload;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
