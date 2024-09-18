package org.example.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.entity.TrainingTypeEntity;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class TrainingDto {
    private Long trainerId;
    private Long traineeId;
    private String trainingName;
    private TrainingTypeEntity trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

}
