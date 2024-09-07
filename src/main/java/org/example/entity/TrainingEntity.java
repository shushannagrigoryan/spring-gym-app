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
     * @param traineeId        The id of the Trainee.
     * @param trainerId        The id of the Trainer.
     * @param trainingName     The name of the Training.
     * @param trainingType     The type of the Training.
     * @param trainingDate     The date of the Training.
     * @param trainingDuration The duration of the Training.
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
