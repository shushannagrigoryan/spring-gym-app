package org.example.dao;

import java.util.Optional;
import java.util.OptionalLong;
import org.example.entity.TrainingEntity;
import org.example.storage.DataStorage;
import org.springframework.stereotype.Component;

@Component
public class TrainingDao {
    private final DataStorage dataStorage;

    public TrainingDao(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /** Generates an id for Training entity and adds the entity to the storage map.*/
    public void createTraining(TrainingEntity trainingEntity) {
        Long id = generateId();
        trainingEntity.setTrainingId(id);
        dataStorage.getTrainingStorage().put(id, trainingEntity);
    }

    /** Generates a unique id for the Training entity.*/
    public Long generateId() {
        OptionalLong lastId = dataStorage.getTrainingStorage().values().stream()
                .mapToLong(TrainingEntity::getTrainingId)
                .max();
        if (lastId.isPresent()) {
            return lastId.getAsLong() + 1;
        } else {
            return 0L;
        }
    }

    public Optional<TrainingEntity> getTrainingById(Long id) {
        return Optional.ofNullable(dataStorage.getTrainingStorage().get(id));
    }
}
