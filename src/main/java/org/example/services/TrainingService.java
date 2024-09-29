package org.example.services;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingTypeService trainingTypeService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainingService(TrainingRepository trainingRepository,
                           TrainingTypeService trainingTypeService,
                           TrainerService trainerService,
                           TraineeService traineeService) {
        this.trainingRepository = trainingRepository;
        this.trainingTypeService = trainingTypeService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }

    /**
     * Creates training in service layer.
     * If either trainee or trainer is not found throws an {@code GymIllegalIdException}
     *
     * @param trainingEntity {@code TrainingEntity} to create the trainee
     */
    @Transactional
    public void createTraining(TrainingEntity trainingEntity) {
        log.debug("Creating training : {}", trainingEntity);
        trainingEntity.setTrainee(traineeService.getTraineeById(trainingEntity.getTraineeId()));
        trainingEntity.setTrainer(trainerService.getTrainerById(trainingEntity.getTrainerId()));
        trainingEntity.setTrainingType(trainingTypeService.getTrainingTypeById(trainingEntity.getTrainingTypeId()));

        trainingRepository.createTraining(trainingEntity);
        log.debug("Successfully created new training with id: {}", trainingEntity.getId());
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
        TrainingEntity training = trainingRepository.getTrainingById(id);
        if (training == null) {
            log.debug("Invalid id for training: {}", id);
            throw new GymIllegalIdException(String.format("No training with id: %d", id));
        }
        log.debug("Getting training with id: {}", id);
        return training;
    }


    /**
     * Updates training by id.
     *
     * @param trainingId     training id
     * @param trainingEntity new training data
     */
    @Transactional
    public void updateTraining(Long trainingId, TrainingEntity trainingEntity) {
        TrainingEntity training = trainingRepository.getTrainingById(trainingId);
        if (training == null) {
            throw new GymIllegalIdException(String.format("No training with id: %d", trainingId));
        }

        training.setTrainingName(trainingEntity.getTrainingName());
        training.setTrainingType(trainingTypeService.getTrainingTypeById(trainingEntity.getTrainingTypeId()));
        training.setTrainingDate(trainingEntity.getTrainingDate());
        training.setTrainingDuration(trainingEntity.getTrainingDuration());
        training.setTrainee(traineeService.getTraineeById(trainingEntity.getTraineeId()));
        training.setTrainer(trainerService.getTrainerById(trainingEntity.getTrainerId()));

        trainingRepository.updateTraining(training);
    }

    /**
     * Returns trainers trainings list by trainer username and given criteria.
     *
     * @param trainerUsername username of the trainer
     * @param fromDate        training fromDate
     * @param toDate          training toDate
     * @param traineeUsername trainee username
     * @return {@code List<TrainingEntity>}
     */

    @Transactional
    public List<TrainingEntity> getTrainerTrainingsByFilter(String trainerUsername, LocalDate fromDate,
                                                            LocalDate toDate, String traineeUsername) {

        return trainingRepository.getTrainerTrainingsByFilter(trainerUsername, fromDate,
                toDate, traineeUsername);
    }

    /**
     * Returns trainees trainings list by trainee username and given criteria.
     *
     * @param traineeUsername username of the trainee
     * @param fromDate        training fromDate
     * @param toDate          training toDate
     * @param trainingTypeId  training type
     * @param trainerUsername trainer username
     * @return {@code List<TrainingEntity>}
     */

    @Transactional
    public List<TrainingEntity> getTraineeTrainingsByFilter(String traineeUsername, LocalDate fromDate,
                                                            LocalDate toDate, Long trainingTypeId,
                                                            String trainerUsername) {
        log.debug("Getting trainee trainings by filter");
        return trainingRepository.getTraineeTrainingsByFilter(traineeUsername, fromDate,
                toDate, trainingTypeId, trainerUsername);
    }
}
