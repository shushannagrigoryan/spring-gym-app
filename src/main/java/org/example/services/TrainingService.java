package org.example.services;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TrainingDao;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.mapper.TrainingMapper;
import org.example.storage.SaveDataToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingService {
    @Autowired
    private TrainingDao trainingDao;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private SaveDataToFile saveDataToFile;
    private TrainingMapper trainingMapper;

    /**
     * Setting dependencies for TrainingService.
     */
    @Autowired
    public void setDependencies(TraineeService traineeService, TrainerService trainerService,
                                SaveDataToFile saveDataToFile, TrainingMapper trainingMapper) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.saveDataToFile = saveDataToFile;
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
        TraineeDto traineeDto = traineeService.getTraineeById(trainingEntity.getTraineeId());
        TrainerDto trainerDto = trainerService.getTrainerById(trainingEntity.getTrainerId());

        if (traineeDto == null) {
            log.debug("Invalid id for trainee: {}", trainingEntity.getTraineeId());
            throw new GymIllegalIdException(String.format("No trainee with id: %d",
                    trainingEntity.getTraineeId()));
        }
        if (trainerDto == null) {
            log.debug("Invalid id for trainer: {}", trainingEntity.getTrainerId());
            throw new GymIllegalIdException(String.format("No trainer with id: %d",
                    trainingEntity.getTrainerId()));
        }

        trainingDao.createTraining(trainingEntity);
        log.debug("Successfully created new training with id: {}", trainingEntity.getTrainingId());
        saveDataToFile.writeMapToFile("Training");
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
