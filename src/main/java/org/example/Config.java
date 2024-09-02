package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:application.properties")
public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    @Value("${trainee.storage}")
    private String traineeStorageFile;

    @Value("${trainer.storage}")
    private String trainerStorageFile;

    @Value("${training.storage}")
    private String trainingStorageFile;

    @Bean
    public Map<String, Trainee> traineeMap(){
        logger.debug("Creating a bean for trainee storage.");
        return new HashMap<>();
    }

    @Bean
    public Map<String, Trainer> trainerMap(){
        logger.debug("Creating a bean for trainer storage.");
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingMap(){
        logger.debug("Creating a bean for training storage.");
        return new HashMap<>();
    }

    @PostConstruct
    private void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            File traineeFile = new File(traineeStorageFile);
            File trainerFile = new File(trainerStorageFile);
            File trainingFile = new File(trainingStorageFile);

            if (traineeFile.length() > 0){
                logger.debug("Initializing trainee storage map from file data.");
                traineeMap().putAll(objectMapper.readValue(traineeFile, new TypeReference<Map<String, Trainee>>(){}));
            }
            if (trainerFile.length() > 0){
                logger.debug("Initializing trainer storage map from file data.");
                trainerMap().putAll(objectMapper.readValue(trainerFile, new TypeReference<Map<String, Trainer>>(){}));
            }
            if (trainingFile.length() > 0){
                logger.debug("Initializing training storage map from file data.");
                trainingMap().putAll(objectMapper.readValue(trainingFile, new TypeReference<Map<Long, Training>>(){}));
            }
        } catch (IOException e) {
            logger.debug("Failed to read  files: {}, {}, {}. Exception: {}",
                    traineeStorageFile, trainerStorageFile, trainingStorageFile, e.getMessage());
        }
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
