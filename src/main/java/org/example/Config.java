//package org.example;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PostConstruct;
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingEntity;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//
//@Configuration
//@ComponentScan(basePackages = "org.example")
//@PropertySource("classpath:application.properties")
//@Slf4j
//public class Config {
//
//    @Value("${trainee.storage}")
//    private String traineeStorageFile;
//
//    @Value("${trainer.storage}")
//    private String trainerStorageFile;
//
//    @Value("${training.storage}")
//    private String trainingStorageFile;
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
//
//    @Bean
//    public Map<Long, TraineeEntity> traineeMap() {
//        log.debug("Creating a bean for trainee storage.");
//        return new HashMap<>();
//    }
//
//    @Bean
//    public Map<Long, TrainerEntity> trainerMap() {
//        log.debug("Creating a bean for trainer storage.");
//        return new HashMap<>();
//    }
//
//    @Bean
//    public Map<Long, TrainingEntity> trainingMap() {
//        log.debug("Creating a bean for training storage.");
//        return new HashMap<>();
//    }
//
//    @PostConstruct
//    private void init() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
//        try {
//            File traineeFile = new File(traineeStorageFile);
//            File trainerFile = new File(trainerStorageFile);
//            File trainingFile = new File(trainingStorageFile);
//
//            if (traineeFile.length() > 0) {
//                log.debug("Initializing trainee storage map from file data.");
//                traineeMap()
//                .putAll(objectMapper.readValue(traineeFile, new TypeReference<Map<Long, TraineeEntity>>() {
//                }));
//            }
//            if (trainerFile.length() > 0) {
//                log.debug("Initializing trainer storage map from file data.");
//                trainerMap()
//                .putAll(objectMapper.readValue(trainerFile, new TypeReference<Map<Long, TrainerEntity>>() {
//                }));
//            }
//            if (trainingFile.length() > 0) {
//                log.debug("Initializing training storage map from file data.");
//                trainingMap().putAll(objectMapper.readValue(trainingFile,
//                        new TypeReference<Map<Long, TrainingEntity>>() {}));
//            }
//        } catch (IOException e) {
//            log.debug("Failed to read  files: {}, {}, {}. Exception: {}",
//                    traineeStorageFile, trainerStorageFile, trainingStorageFile, e.getMessage());
//        }
//    }
//}
