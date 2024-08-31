package org.example.model;
import org.example.TrainingType;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;


public class Training {
    private Long trainingId;
    private Long trainerId;
    private Long traineeId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

    public Training(Long trainerId, Long traineeId, String trainingName,
                    TrainingType trainingType, LocalDateTime trainingDate, Duration trainingDuration) {
        this.trainerId = trainerId;
        this.traineeId = traineeId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public long getTrainingId() {
        return this.trainingId;
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainingId=" + trainingId +
                ", trainerId=" + trainerId +
                ", traineeId=" + traineeId +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }
}
