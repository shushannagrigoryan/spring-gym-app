package org.example.dao;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.storage.DataStorage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainingDao {
    private final DataStorage dataStorage;

    private final IdGenerator idGenerator;


    public TrainingDao(DataStorage dataStorage, IdGenerator idGenerator) {
        this.dataStorage = dataStorage;
        this.idGenerator = idGenerator;
    }

    /**
     * Generates id for the training entity and adds that entity to the storage map.
     *
     * @param trainingEntity {@code TrainingEntity} to be added to storage
     */
    public void createTraining(TrainingEntity trainingEntity) {
        Long id = idGenerator.generateId("Training");
        log.debug("Saving training: {} to storage", trainingEntity);
        trainingEntity.setTrainingId(id);
        dataStorage.getTrainingStorage().put(id, trainingEntity);
    }


    /**
     * Gets training by id.
     *
     * @param id id of the training
     * @return {@code Optional<TrainingEntity>}
     */
    public Optional<TrainingEntity> getTrainingById(Long id) {
        log.debug("Getting training with id: {}", id);
        return Optional.ofNullable(dataStorage.getTrainingStorage().get(id));
    }
}
