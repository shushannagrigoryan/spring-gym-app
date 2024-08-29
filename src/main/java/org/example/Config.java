package org.example;

import org.example.model.Trainee;
import org.example.model.Trainer;
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
    public Map<String, Map<String, Object>> storageMap(){
        Map<String, Map<String, Object>>  map = new HashMap<>();
        map.put("Trainee", new HashMap<>());
        map.put("Trainer", new HashMap<>());
        map.put("Training", new HashMap<>());
        return map;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
