package org.example;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:application.properties")
public class Config {

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

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
