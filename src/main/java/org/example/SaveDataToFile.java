package org.example;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.entity.TraineeEntity;
//import org.example.entity.Trainer;
import org.example.entity.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Component
public class SaveDataToFile {
    private static final Logger logger = LoggerFactory.getLogger(SaveDataToFile.class);
    @Autowired
    private  Map<Long, TraineeEntity> traineeStorage;

    @Value("${trainee.storage}")
    private  String traineeStorageFile;

//    @Autowired
//    private  Map<String, Trainer> trainerStorage;

    @Value("${trainer.storage}")
    private  String trainerStorageFile;

    @Autowired
    private  Map<Long, Training> trainingStorage;

    @Value("${training.storage}")
    private  String trainingStorageFile;
    public  void writeMapToFile(String dataType) {
        String fileName;
        Object storageMap;
        switch (dataType) {
            case "Trainee":
                storageMap = traineeStorage;
                fileName = traineeStorageFile;
                break;
//            case "Trainer":
//                storageMap = trainerStorage;
//                fileName = trainerStorageFile;
//                break;
            case "Training":
                storageMap = trainingStorage;
                fileName = trainingStorageFile;
                break;
            default:
                throw new IllegalArgumentException("Illegal storage name: " + dataType);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(writer.writeValueAsString(storageMap));
            logger.debug("Writing map storage for " + dataType +  " entity to file.");
        } catch (IOException e) {
            logger.debug("Failed to write map data to file: {}. Exception: {}",
                    storageMap, e.getMessage());
        }
    }
}
