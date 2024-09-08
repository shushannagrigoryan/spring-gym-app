package org.example.storage;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.FileWriter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.GymIllegalEntityTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SaveDataToFile {
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
            default -> throw new GymIllegalEntityTypeException(dataType);
        };
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(writer.writeValueAsString(storageMap));
            log.debug("Writing map storage for {} entity to file.", dataType);
        } catch (IOException e) {
            log.debug("Failed to write map data to file: {}. Exception: {}",
                    storageMap, e.getMessage());
        }
    }
}
