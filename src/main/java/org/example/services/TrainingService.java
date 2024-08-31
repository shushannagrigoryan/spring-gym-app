package org.example.services;

import org.example.MapDataClass;
import org.example.TrainingType;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.OptionalLong;

@Service
public class TrainingService {
    @Autowired
    private MapDataClass storageMap;
    private TraineeService traineeService;
    private TrainerService trainerService;

    @Autowired
    public void setDependencies(TraineeService traineeService, TrainerService trainerService){
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }
    public void createTraining(String traineeUsername,String trainerUsername, String trainingName, TrainingType trainingType,
                               LocalDateTime trainingDate, Duration trainingDuration){
        try{
            Trainee trainee = traineeService.getTrainee(traineeUsername);
            Trainer trainer = trainerService.getTrainer(trainerUsername);
            Training training = new Training(trainer.getUserID(), trainee.getUserID(), trainingName,
                    trainingType, trainingDate, trainingDuration);
            Long trainingId = generateId();
            training.setTrainingId(trainingId);
            storageMap.getTrainingMap().put(trainingId, training);

        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(storageMap.getTrainingMap());

    }

    private Long generateId(){
        OptionalLong lastId = storageMap.getTrainingMap().values().stream()
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
        Training training = storageMap.getTrainingMap().get(trainingId);
        if (training == null){
            throw new IllegalArgumentException("No such with id: " + trainingId);
        }
        return training;
    }


}
