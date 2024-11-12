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
public class TraineeTrainingsFilterRequestDto {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String trainerUsername;
    private Long trainingType;

}
