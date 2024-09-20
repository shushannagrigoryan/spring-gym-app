package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingService {
    private final TrainingDao trainingDao;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final TrainerDao trainerDao;
    private final TraineeDao traineeDao;


    /**
     * Injecting dependencies using constructor.
     */
    public TrainingService(TrainingDao trainingDao,
                           TraineeService traineeService,
                           TrainerService trainerService,
                           TraineeDao traineeDao,
                           TrainerDao trainerDao,
                           TrainingTypeService trainingTypeService) {
        this.trainingDao = trainingDao;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.trainingTypeService = trainingTypeService;
    }

    /**
     * Creates training in service layer.
     * If either trainee or trainer is not found throws an {@code GymIllegalIdException}
     *
     * @param trainingEntity {@code TrainingEntity} to create the trainee
     */
    public void createTraining(TrainingEntity trainingEntity) {
        log.debug("Creating training : {}", trainingEntity);

        //Optional<TrainerEntity> trainer = trainerDao.getTrainerById(trainingEntity.getTrainerId());
        //Optional<TraineeEntity> trainee = traineeDao.getTraineeById(trainingEntity.getTraineeId());

        trainerService.getTrainerById(trainingEntity.getTrainerId());
        traineeService.getTraineeById(trainingEntity.getTraineeId());

        trainingTypeService.getTrainingTypeById(trainingEntity.getTrainingTypeId());

        trainingDao.createTraining(trainingEntity);
        log.debug("Successfully created new training with id: {}", trainingEntity.getId());

    }

    //    /**
    //     * Gets training by id.
    //     *
    //     * @param id of the training
    //     * @return the {@code TrainingDto}
    //     */
    //    public TrainingDto getTrainingById(Long id) {
    //        log.debug("Retrieving training by id: {}", id);
    //        Optional<TrainingEntity> training = trainingDao.getTrainingById(id);
    //        if (training.isEmpty()) {
    //            log.debug("Invalid id for training: {}", id);
    //            throw new GymIllegalIdException(String.format("No training with id: %d", id));
    //        }
    //        log.debug("Getting training with id: {}", id);
    //        return trainingMapper.entityToDto(training.get());
    //    }


}
