package org.example.dto;

import lombok.*;
import org.example.TrainingType;

import java.time.Duration;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class TrainingDto {
    private Long trainerId;
    private  Long traineeId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

}
