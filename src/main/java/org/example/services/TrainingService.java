package org.example.services;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;
import org.example.dao.TrainingTypeDao;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.mapper.TrainingMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingService {
    private final TrainingDao trainingDao;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final TrainingMapper trainingMapper;
    private final TrainerDao trainerDao;
    private final TraineeDao traineeDao;
    private final TrainingTypeDao trainingTypeDao;


    /**
     * Injecting dependencies using constructor.
     */
    public TrainingService(TrainingDao trainingDao,
                           TraineeService traineeService,
                           TrainerService trainerService,
                           TraineeDao traineeDao,
                           TrainerDao trainerDao,
                           TrainingTypeService trainingTypeService,
                           TrainingTypeDao trainingTypeDao,
                           TrainingMapper trainingMapper) {
        this.trainingDao = trainingDao;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeDao = trainingTypeDao;
        this.trainingMapper = trainingMapper;
    }

    /**
     * Creates training in service layer.
     * If either trainee or trainer is not found throws an {@code GymIllegalIdException}
     *
     * @param trainingEntity {@code TrainingEntity} to create the trainee
     */
    public void createTraining(TrainingEntity trainingEntity) {
        log.debug("Creating training : {}", trainingEntity);
        Long trainingTypeId = trainingEntity.getTrainingTypeId();
        Long trainerId = trainingEntity.getTrainerId();
        Long traineeId = trainingEntity.getTraineeId();

        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(trainerId);
        Optional<TraineeEntity> trainee = traineeDao.getTraineeById(traineeId);
        Optional<TrainingTypeEntity> trainingType =
                trainingTypeDao.getTrainingTypeById(trainingTypeId);

        trainingType.ifPresentOrElse(trainingEntity::setTrainingType, () -> {
            throw new GymIllegalIdException(
                    String.format("TrainingType with id %d does not exist", trainingTypeId));
        });

        trainer.ifPresentOrElse(trainingEntity::setTrainer, () -> {
            throw new GymIllegalIdException(
                    String.format("Trainer with id %d does not exist", trainerId));
        });

        trainee.ifPresentOrElse(trainingEntity::setTrainee, () -> {
            throw new GymIllegalIdException(
                    String.format("Trainee with id %d does not exist", traineeId));
        });

        trainingDao.createTraining(trainingEntity);
        log.debug("Successfully created new training with id: {}", trainingEntity.getId());

    }

    /**
     * Gets training by id.
     *
     * @param id of the training
     * @return the {@code TrainingDto}
     */
    public TrainingDto getTrainingById(Long id) {
        log.debug("Retrieving training by id: {}", id);
        Optional<TrainingEntity> training = trainingDao.getTrainingById(id);
        if (training.isEmpty()) {
            log.debug("Invalid id for training: {}", id);
            throw new GymIllegalIdException(String.format("No training with id: %d", id));
        }
        log.debug("Getting training with id: {}", id);
        return trainingMapper.entityToDto(training.get());
    }


}
