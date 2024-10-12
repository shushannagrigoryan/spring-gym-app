package org.example.services;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TraineeTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainerTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainingCreateRequestDto;
import org.example.entity.TrainingEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.repository.TrainingCriteriaRepository;
import org.example.repository.TrainingRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingCriteriaRepository trainingCriteriaRepository;
    private final TrainingTypeService trainingTypeService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainingService(TrainingRepository trainingRepository,
                           TrainingTypeService trainingTypeService,
                           TrainerService trainerService,
                           TraineeService traineeService,
                           TrainingCriteriaRepository trainingCriteriaRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingTypeService = trainingTypeService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingCriteriaRepository = trainingCriteriaRepository;
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
        training.setTrainingName(trainingCreateDto.getTrainingName());
        training.setTrainingDate(trainingCreateDto.getTrainingDate());
        training.setTrainingDuration(trainingCreateDto.getTrainingDuration());
        training.setTrainingType(trainingTypeService.getTrainingTypeById(trainingCreateDto.getTrainingType()));
        training.setTrainee(traineeService.getTraineeByUsername(trainingCreateDto.getTraineeUsername()));
        training.setTrainer(trainerService.getTrainerByUsername(trainingCreateDto.getTrainerUsername()));

        TrainingEntity createdTraining = trainingRepository.save(training);
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
    public List<TrainingEntity> getTraineeTrainingsByFilter(TraineeTrainingsFilterRequestDto traineeTrainings) {
        log.debug("Getting trainee trainings by filter");
        return trainingCriteriaRepository
                .getTraineeTrainingsByFilter(traineeTrainings.getTraineeUsername(),
                        traineeTrainings.getFromDate(),
                        traineeTrainings.getToDate(), traineeTrainings.getTrainingType(),
                        traineeTrainings.getTrainerUsername());
    }

    /**
     * Returns trainer trainings by the given criteria.
     *
     * @param trainerTrainings {@code TrainerTrainingsFilterRequestDto}
     * @return {@code List<TrainingEntity>}
     */
    @Transactional
    public List<TrainingEntity> getTrainerTrainingsByFilter(TrainerTrainingsFilterRequestDto trainerTrainings) {
        log.debug("Getting trainer trainings by filter");
        return trainingCriteriaRepository.getTrainerTrainingsByFilter(trainerTrainings.getTrainerUsername(),
                trainerTrainings.getFromDate(),
                trainerTrainings.getToDate(),
                trainerTrainings.getTraineeUsername());
    }
}
