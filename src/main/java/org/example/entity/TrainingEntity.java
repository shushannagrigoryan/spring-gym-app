package org.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.TrainingType;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TrainingEntity {
    private Long trainingId;
    private Long trainerId;
    private  Long traineeId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

}
