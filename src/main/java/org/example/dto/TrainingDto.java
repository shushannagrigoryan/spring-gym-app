package org.example.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class TrainingDto {
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private Long trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

}
