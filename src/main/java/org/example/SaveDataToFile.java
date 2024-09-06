package org.example;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.example.storage.DataStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SaveDataToFile {
    private static final Logger logger = LoggerFactory.getLogger(SaveDataToFile.class);
    @Autowired
    private DataStorage dataStorage;

    @Value("${trainee.storage}")
    private String traineeStorageFile;

    @Value("${trainer.storage}")
    private String trainerStorageFile;

    @Value("${training.storage}")
    private String trainingStorageFile;

    /**
     * Writing the storage map to a json file.
     *
     * @param dataType data type for the storage file(Trainer, Trainee, Training)
     */
    public void writeMapToFile(String dataType) {
        String fileName;
        Object storageMap;
        fileName = switch (dataType) {
            case "Trainee" -> {
                storageMap = dataStorage.getTraineeStorage();
                yield traineeStorageFile;
            }
            case "Trainer" -> {
                storageMap = dataStorage.getTrainerStorage();
                yield trainerStorageFile;
            }
            case "Training" -> {
                storageMap = dataStorage.getTrainingStorage();
                yield trainingStorageFile;
            }
            default -> throw new IllegalArgumentException("Illegal storage name: " + dataType);
        };
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(writer.writeValueAsString(storageMap));
            logger.debug("Writing map storage for " + dataType + " entity to file.");
        } catch (IOException e) {
            logger.debug("Failed to write map data to file: {}. Exception: {}",
                    storageMap, e.getMessage());
        }
    }
}
