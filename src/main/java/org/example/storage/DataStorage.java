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
    @Value("${trainee.storage}")
    private String traineeStorageFile;

    @Value("${trainer.storage}")
    private String trainerStorageFile;

    @Value("${training.storage}")
    private String trainingStorageFile;

    private final Map<Long, TraineeEntity> traineeStorage = new HashMap<>();
    private final Map<Long, TrainerEntity> trainerStorage = new HashMap<>();
    private final Map<Long, TrainingEntity> trainingStorage = new HashMap<>();


    @PostConstruct
    private void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            File traineeFile = new File(traineeStorageFile);
            File trainerFile = new File(trainerStorageFile);
            File trainingFile = new File(trainingStorageFile);

            if (traineeFile.length() > 0) {
                log.debug("Initializing trainee storage map from file data.");
                traineeStorage
                        .putAll(objectMapper.readValue(traineeFile, new TypeReference<Map<Long, TraineeEntity>>() {
                        }));
            }
            if (trainerFile.length() > 0) {
                log.debug("Initializing trainer storage map from file data.");
                trainerStorage
                        .putAll(objectMapper.readValue(trainerFile, new TypeReference<Map<Long, TrainerEntity>>() {
                        }));
            }
            if (trainingFile.length() > 0) {
                log.debug("Initializing training storage map from file data.");
                trainingStorage.putAll(objectMapper.readValue(trainingFile,
                        new TypeReference<Map<Long, TrainingEntity>>() {
                        }));
            }
        } catch (IOException e) {
            log.debug("Failed to read  files: {}, {}, {}. Exception: {}",
                    traineeStorageFile, trainerStorageFile, trainingStorageFile, e.getMessage());
        }
    }
}
