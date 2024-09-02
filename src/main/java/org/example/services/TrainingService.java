package org.example.services;

import org.example.SaveDataToFile;
import org.example.TrainingType;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.OptionalLong;

@Service
public class TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    private Map<Long, Training> trainingMap;
    private TraineeService traineeService;
    private TrainerService trainerService;

    private SaveDataToFile saveDataToFile;

    @Autowired
    public void setDependencies(TraineeService traineeService, TrainerService trainerService,
                                SaveDataToFile saveDataToFile){
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.saveDataToFile = saveDataToFile;
    }
    public void createTraining(String traineeUsername,String trainerUsername, String trainingName, TrainingType trainingType,
                               LocalDateTime trainingDate, Duration trainingDuration){
        logger.debug("Creating training with traineeUsername: {}, trainerUsername: {}",
                traineeUsername, trainerUsername);
        try{
            Trainee trainee = traineeService.getTrainee(traineeUsername);
            Trainer trainer = trainerService.getTrainer(trainerUsername);
            Training training = new Training(trainer.getUserId(), trainee.getUserId(), trainingName,
                    trainingType, trainingDate, trainingDuration);
            Long trainingId = generateId();
            training.setTrainingId(trainingId);
            trainingMap.put(trainingId, training);
            logger.debug("Created training: traineeUsername = " + traineeUsername + " trainerUsername = " + trainerUsername);
            saveDataToFile.writeMapToFile("Training");

        }catch(Exception e){
            logger.debug("Failed to create training: traineeUsername = " + traineeUsername + " trainerUsername = " + trainerUsername);
        }
    }

    private Long generateId(){
        OptionalLong lastId = trainingMap.values().stream()
                .mapToLong(Training::getTrainingId)
                .max();
        if(lastId.isPresent()){
            return lastId.getAsLong() + 1;
        }
        else{
            return 0L;
        }
    }

    public Training getTraining(Long trainingId){
        Training training = trainingMap.get(trainingId);
        if (training == null){
            throw new IllegalArgumentException("No such with id: " + trainingId);
        }
        logger.debug("Getting training with id: " + trainingId);
        return training;
    }


}
