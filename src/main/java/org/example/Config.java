package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
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

    @Value("${trainee.storage}")
    private String traineeStorageFile;

    @Bean
    public Map<String, Trainee> traineeMap(){
        return new HashMap<>();
    }

    @Bean
    public Map<String, Trainer> trainerMap(){
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingMap(){
        return new HashMap<>();
    }

    @PostConstruct
    private void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            File file = new File(traineeStorageFile);
            if (file.length() > 0){
                traineeMap().putAll(objectMapper.readValue(file, new TypeReference<Map<String, Trainee>>(){}));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
