package org.example.dao;

import java.util.Optional;
import java.util.Set;
import org.example.exceptions.GymIllegalEntityTypeException;
import org.example.storage.DataStorage;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
    private final DataStorage dataStorage;

    public IdGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Generates id for the given entity.
     *
     * @param entityType type fo the entity.
     * @return the id.
     */
    public Long generateId(String entityType) {
        System.out.println("GENERATING ID");
        Set<Long> longList = switch (entityType) {
            case "Trainee" -> dataStorage.getTraineeStorage()
                    .keySet();
            case ("Trainer") -> dataStorage.getTrainerStorage()
                    .keySet();
            case ("Training") -> dataStorage.getTrainingStorage()
                    .keySet();
            default -> throw new GymIllegalEntityTypeException(entityType);
        };

        Optional<Long> lastId = longList.stream().max(Long::compareTo);
        return lastId.map(id -> id + 1).orElse(0L);
    }
}
