package org.example.dao;

import java.util.Optional;
import java.util.OptionalLong;
import org.example.entity.TrainingEntity;
import org.example.storage.DataStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainingDao {
    @Autowired
    private final DataStorage dataStorage;

    public TrainingDao(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Generates id for the training entity and adds that entity to the storage map.
     *
     * @param trainingEntity {@code TrainingEntity} to be added to storage
     */
    public void createTraining(TrainingEntity trainingEntity) {
        Long id = generateId();
        trainingEntity.setTrainingId(id);
        dataStorage.getTrainingStorage().put(id, trainingEntity);
    }

    /**
     * Generates a unique id for the training entity.
     */
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

    /**
     * Gets training by id.
     *
     * @param id id of the training
     * @return {@code Optional<TrainingEntity>}
     */
    public Optional<TrainingEntity> getTrainingById(Long id) {
        return Optional.ofNullable(dataStorage.getTrainingStorage().get(id));
    }
}
