package org.example.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:application.properties")
@Slf4j
@Getter
public class DataStorage {
    private final Map<Long, TraineeEntity> traineeStorage = new HashMap<>();
    private final Map<Long, TrainerEntity> trainerStorage = new HashMap<>();
    private final Map<Long, TrainingEntity> trainingStorage = new HashMap<>();
    private final Map<String, TraineeEntity> traineeStorageUsernameKey = new HashMap<>();
    private final Map<String, TrainerEntity> trainerStorageUsernameKey = new HashMap<>();

    @Value("${trainee.storage}")
    private String traineeStorageFile;
    @Value("${trainer.storage}")
    private String trainerStorageFile;
    @Value("${training.storage}")
    private String trainingStorageFile;

    /**
     * Initializes the storage bean from external files.
     */
    @PostConstruct
    public void init() {
        log.debug("Initializing storage bean from files.");
        System.out.println(1);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(2);
        objectMapper.findAndRegisterModules();
        System.out.println(3);
        try {
            System.out.println(traineeStorageFile);
            File traineeFile = new File(traineeStorageFile);
            File trainerFile = new File(trainerStorageFile);
            File trainingFile = new File(trainingStorageFile);

            System.out.println(4);
            if (traineeFile.length() > 0) {
                initTrainee(objectMapper, traineeFile);
            }
            if (trainerFile.length() > 0) {
                initTrainer(objectMapper, trainerFile);
            }
            if (trainingFile.length() > 0) {
                initTraining(objectMapper, trainingFile);
            }
        } catch (IOException e) {
            log.debug("Failed to read  files: {}, {}, {}. Exception: {}",
                    traineeStorageFile, trainerStorageFile, trainingStorageFile, e.getMessage());
        }
    }

    private void initTrainee(ObjectMapper objectMapper, File traineeFile) throws IOException {
        log.debug("Initializing trainee storage map from file data.");
        traineeStorage
                .putAll(objectMapper.readValue(traineeFile, new TypeReference<Map<Long, TraineeEntity>>() {
                }));

        // populate a different map using username as key
        for (Map.Entry<Long, TraineeEntity> entry : traineeStorage.entrySet()) {
            TraineeEntity trainee = entry.getValue();
            traineeStorageUsernameKey.put(trainee.getUsername(), trainee);
        }
    }

    private void initTrainer(ObjectMapper objectMapper, File trainerFile) throws IOException {
        log.debug("Initializing trainer storage map from file data.");
        trainerStorage
                .putAll(objectMapper.readValue(trainerFile, new TypeReference<Map<Long, TrainerEntity>>() {
                }));

        // populate a different map using username as key
        for (Map.Entry<Long, TrainerEntity> entry : trainerStorage.entrySet()) {
            TrainerEntity trainer = entry.getValue();
            trainerStorageUsernameKey.put(trainer.getUsername(), trainer);
        }
    }

    private void initTraining(ObjectMapper objectMapper, File trainingFile) throws IOException {
        log.debug("Initializing training storage map from file data.");
        trainingStorage.putAll(objectMapper.readValue(trainingFile,
                new TypeReference<Map<Long, TrainingEntity>>() {
                }));
    }
}
