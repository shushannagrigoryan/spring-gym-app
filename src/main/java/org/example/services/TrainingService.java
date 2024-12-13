package org.example.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TraineeTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainerTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainingCreateRequestDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.jpaspecifications.TrainingSpecification;
import org.example.metrics.TrainingMetrics;
import org.example.repositories.TrainingRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingTypeService trainingTypeService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingMetrics trainingMetrics;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainingService(TrainingRepository trainingRepository,
                           TrainingTypeService trainingTypeService,
                           TrainerService trainerService,
                           TraineeService traineeService,
                           TrainingMetrics trainingMetrics) {
        this.trainingRepository = trainingRepository;
        this.trainingTypeService = trainingTypeService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingMetrics = trainingMetrics;
    }

    /**
     * Creates training in service layer.
     * If either trainee or trainer is not found throws an {@code GymEntityNotFoundException}
     * Throws GymIllegalIdException if training type does not exist.
     *
     * @param trainingCreateDto {@code TrainingCreateRequestDto} to create the training
     */
    @Transactional
    public void createTraining(TrainingCreateRequestDto trainingCreateDto) {
        log.debug("Creating training : {}", trainingCreateDto);


        TrainingEntity training = new TrainingEntity();

        TrainerEntity trainer = trainerService.getTrainerByUsername(trainingCreateDto.getTrainerUsername());
        training.setTrainingName(trainingCreateDto.getTrainingName());
        training.setTrainingDate(trainingCreateDto.getTrainingDate());
        training.setTrainingDuration(trainingCreateDto.getTrainingDuration());
        training.setTrainingType(trainingTypeService.getTrainingTypeById(trainer.getSpecialization().getId()));
        training.setTrainee(traineeService.getTraineeByUsername(trainingCreateDto.getTraineeUsername()));
        training.setTrainer(trainer);

        TrainingEntity createdTraining = trainingRepository.save(training);
        trainingMetrics.incrementCounter();
        log.debug("Successfully created new training with id: {}", createdTraining.getId());
    }

    /**
     * Gets training by id.
     *
     * @param id of the training
     * @return the {@code TrainingEntity}
     */
    @Transactional
    public TrainingEntity getTrainingById(Long id) {
        log.debug("Retrieving training by id: {}", id);
        Optional<TrainingEntity> training = trainingRepository.findById(id);
        if (training.isEmpty()) {
            log.debug("Invalid id for training: {}", id);
            throw new GymEntityNotFoundException(String.format("No training with id: %d", id));
        }
        log.debug("Getting training with id: {}", id);
        return training.get();
    }

    /**
     * Returns trainee trainings by the given criteria.
     *
     * @param traineeTrainings {@code TraineeTrainingsFilterRequestDto}
     * @return {@code List<TrainingEntity>}
     */
    @Transactional
    public List<TrainingEntity> getTraineeTrainingsByFilter(String traineeUsername,
                                                            TraineeTrainingsFilterRequestDto traineeTrainings) {
        TraineeEntity trainee = traineeService.getTraineeByUsername(traineeUsername);
        log.debug("Getting trainee: {}  trainings by filter", trainee);

        LocalDateTime fromDate =
            traineeTrainings.getFromDate() == null ? null : LocalDateTime.parse(traineeTrainings.getFromDate());
        LocalDateTime toDate =
            traineeTrainings.getToDate() == null ? null : LocalDateTime.parse(traineeTrainings.getToDate());

        Long trainingType =
            traineeTrainings.getTrainingType() == null ? null : Long.parseLong(traineeTrainings.getTrainingType());
        Specification<TrainingEntity> specification = Specification.where(
                TrainingSpecification.hasTraineeUsername(traineeUsername))
            .and(TrainingSpecification.hasTrainingDateBetween(fromDate, toDate))
            .and(TrainingSpecification.hasTrainingType(trainingType))
            .and(TrainingSpecification.hasTrainerUsername(traineeTrainings.getTrainerUsername()));

        return trainingRepository.findAll(specification);
    }

    /**
     * Returns trainer trainings by the given criteria.
     *
     * @param trainerTrainings {@code TrainerTrainingsFilterRequestDto}
     * @return {@code List<TrainingEntity>}
     */
    @Transactional
    public List<TrainingEntity> getTrainerTrainingsByFilter(String trainerUsername,
                                                            TrainerTrainingsFilterRequestDto trainerTrainings) {
        TrainerEntity trainer = trainerService.getTrainerByUsername(trainerUsername);
        log.debug("Getting trainer: {} trainings by filter", trainer);
        LocalDateTime fromDate =
            trainerTrainings.getFromDate() == null ? null : LocalDateTime.parse(trainerTrainings.getFromDate());
        LocalDateTime toDate =
            trainerTrainings.getToDate() == null ? null : LocalDateTime.parse(trainerTrainings.getToDate());
        Specification<TrainingEntity> specification = Specification.where(
            TrainingSpecification.hasTrainerUsername(trainerUsername)
                .and(TrainingSpecification.hasTrainingDateBetween(fromDate, toDate))
                .and(TrainingSpecification.hasTraineeUsername(trainerTrainings.getTraineeUsername()))
        );

        return trainingRepository.findAll(specification);
    }

    /**
     * Checking if training exists with the given trainee and trainer.
     *
     * @param traineeUsername trainee username
     * @param trainerUsername trainer username
     * @return {@code boolean} true if training exists, else false
     */

    @Transactional
    public boolean trainingExists(String traineeUsername, String trainerUsername) {
        log.debug("Checking if a training exists with the given trainee and trainer.");
        return trainingRepository.existsByTrainee_User_UsernameAndTrainer_User_Username(
            traineeUsername, trainerUsername);
    }
}
