package org.example.model;
import org.example.TrainingType;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;


public class Training {
    private Long trainerId;
    private Long traineeId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime date;
    private Duration duration;

}
