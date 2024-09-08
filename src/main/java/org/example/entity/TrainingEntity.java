package org.example.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TrainingEntity {
    private Long trainingId;
    private Long trainerId;
    private Long traineeId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

    /**
     * Constructs a new instance of {@code TrainingEntity}.
     *
     * @param traineeId        The id of the trainee.
     * @param trainerId        The id of the trainer.
     * @param trainingName     The name of the training.
     * @param trainingType     The type of the training.
     * @param trainingDate     The date of the training.
     * @param trainingDuration The duration of the training.
     */
    public TrainingEntity(Long traineeId, Long trainerId,
                          String trainingName, TrainingType trainingType,
                          LocalDateTime trainingDate, Duration trainingDuration) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

}
