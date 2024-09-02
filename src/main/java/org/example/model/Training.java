package org.example.model;

import org.example.TrainingType;

import java.time.Duration;
import java.time.LocalDateTime;


public class Training {
    private Long trainingId;
    private Long trainerId;
    private  Long traineeId;
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

    public Training(){

    }

    public long getTrainingId() {
        return this.trainingId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public LocalDateTime getTrainingDate() {
        return trainingDate;
    }

    public Duration getTrainingDuration() {
        return trainingDuration;
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
                ", trainingDuration=" + trainingDuration.toString() +
                '}';
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }
}
