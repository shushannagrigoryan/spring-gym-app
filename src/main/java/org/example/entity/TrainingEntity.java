package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trainings")
public class TrainingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_name")
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingTypeEntity trainingType;

    @Column(name = "training_date")
    private LocalDateTime trainingDate;

    @Column(name = "training_duration", nullable = false)
    private Duration trainingDuration;

    @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = false)
    private TraineeEntity trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private TrainerEntity trainer;

    @Transient
    private Long traineeId;
    @Transient
    private Long trainerId;
    @Transient
    private Long trainingTypeId;

    /**
     * Constructs a new instance of {@code TrainingEntity}.
     *
     * @param traineeId        The id of the trainee.
     * @param trainerId        The id of the trainer.
     * @param trainingName     The name of the training.
     * @param trainingTypeId     The type of the training.
     * @param trainingDate     The date of the training.
     * @param trainingDuration The duration of the training.
     */
    public TrainingEntity(Long traineeId, Long trainerId,
                          String trainingName, Long trainingTypeId,
                          LocalDateTime trainingDate, Duration trainingDuration) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingTypeId = trainingTypeId;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

}
