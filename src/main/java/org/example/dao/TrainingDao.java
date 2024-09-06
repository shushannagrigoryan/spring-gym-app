package org.example.dao;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Component;

@Component
public class TrainingDao {
    private final Map<Long, TrainingEntity> trainingStorage;

    public TrainingDao(Map<Long, TrainingEntity> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    /** Generates an id for Training entity and adds the entity to the storage map.*/
    public void createTraining(TrainingEntity trainingEntity) {
        Long id = generateId();
        trainingEntity.setTrainingId(id);
        trainingStorage.put(id, trainingEntity);
    }

    /** Generates a unique id for the Training entity.*/
    public Long generateId() {
        OptionalLong lastId = trainingStorage.values().stream()
                .mapToLong(TrainingEntity::getTrainingId)
                .max();
        if (lastId.isPresent()) {
            return lastId.getAsLong() + 1;
        } else {
            return 0L;
        }
    }

    public Optional<TrainingEntity> getTrainingById(Long id) {
        return Optional.ofNullable(trainingStorage.get(id));
    }
}
