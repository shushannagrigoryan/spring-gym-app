package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.SaveDataToFile;
import org.example.dao.TrainingDao;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.example.exceptions.IllegalIdException;
import org.example.mapper.TrainingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TrainingService {
    @Autowired
    private TrainingDao trainingDao;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private SaveDataToFile saveDataToFile;
    private TrainingMapper trainingMapper;
    @Autowired
    public void setDependencies(TraineeService traineeService, TrainerService trainerService,
                                SaveDataToFile saveDataToFile, TrainingMapper trainingMapper){
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.saveDataToFile = saveDataToFile;
        this.trainingMapper = trainingMapper;
    }

    public void createTraining(TrainingEntity trainingEntity){
        log.debug("Creating training : {}", trainingEntity);
        System.out.println(trainingEntity.getTrainerId());
        TraineeDto traineeDto = traineeService.getTraineeById(trainingEntity.getTraineeId());
        TrainerDto trainerDto = trainerService.getTrainerById(trainingEntity.getTrainerId());

        if(traineeDto == null){
            log.debug("Invalid id for trainee: {}", trainingEntity.getTraineeId());
            throw new IllegalIdException("No trainee with id: " + trainingEntity.getTraineeId());
        }
        if(trainerDto == null){
            log.debug("Invalid id for trainer: {}", trainingEntity.getTrainerId());
            throw new IllegalIdException("No trainer with id: " + trainingEntity.getTrainerId());
        }

        trainingDao.createTraining(trainingEntity);
        log.debug("Successfully created new training with id: "+ trainingEntity.getTrainingId());
        saveDataToFile.writeMapToFile("Training");
    }

    public TrainingDto getTrainingById(Long id){
        log.debug("Retrieving training by id: {}", id);
        Optional<TrainingEntity> training = trainingDao.getTrainingById(id);
        if (!training.isPresent()){
            log.debug("Invalid id for training: {}", id);
            throw new IllegalIdException("No training with id: " + id);
        }
        log.debug("Getting training with id: " + id);
        return trainingMapper.entityToDto(training.get());
    }


}
