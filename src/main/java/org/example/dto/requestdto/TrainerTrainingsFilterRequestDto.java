package org.example.dto.requestdto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingsFilterRequestDto {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String traineeUsername;
}
