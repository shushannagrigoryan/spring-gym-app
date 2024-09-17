package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "trainings")
public class TrainingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Column(name = "trainer_id")
    private Long trainerId;
    @Column(name = "trainee_id")
    private Long traineeId;
    @Column(name = "training_name")
    private String trainingName;
    @Column(name = "training_type_id")
    private Long  trainingType;
    @Column(name = "training_date")
    private LocalDateTime trainingDate;
    @Column(name = "training_duration")
    private Duration trainingDuration;

    @ManyToOne
    @JoinColumn(name = "id")
    private TraineeEntity trainee;

    @ManyToOne
    @JoinColumn(name = "id")
    private TrainerEntity trainer;

    //    /**
    //     * Constructs a new instance of {@code TrainingEntity}.
    //     *
    //     * @param traineeId        The id of the trainee.
    //     * @param trainerId        The id of the trainer.
    //     * @param trainingName     The name of the training.
    //     * @param trainingType     The type of the training.
    //     * @param trainingDate     The date of the training.
    //     * @param trainingDuration The duration of the training.
    //     */
    //    public TrainingEntity(Long traineeId, Long trainerId,
    //                          String trainingName, TrainingType trainingType,
    //                          LocalDateTime trainingDate, Duration trainingDuration) {
    //        this.traineeId = traineeId;
    //        this.trainerId = trainerId;
    //        this.trainingName = trainingName;
    //        this.trainingType = trainingType;
    //        this.trainingDate = trainingDate;
    //        this.trainingDuration = trainingDuration;
    //    }

}
