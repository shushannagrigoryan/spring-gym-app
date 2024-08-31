package org.example;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class MapDataClass {
    private Map<String, Trainee> traineeMap = new HashMap<>();
    private Map<String, Trainer> trainerMap = new HashMap<>();
    private Map<String, Training> trainingMap = new HashMap<>();

    public Map<String, Trainee> getTraineeMap() {
        return traineeMap;
    }

    public Map<String, Trainer> getTrainerMap() {
        return trainerMap;
    }

    public Map<String, Training> getTrainingMap() {
        return trainingMap;
    }
}
