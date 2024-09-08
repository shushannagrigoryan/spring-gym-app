package org.example.dao;

import java.util.Optional;
import java.util.OptionalLong;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.storage.DataStorage;
import org.springframework.stereotype.Component;

@Component
public class TrainerDao {
    private final DataStorage dataStorage;

    public TrainerDao(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Generates id for the trainer entity and adds that entity to the storage map.
     *
     * @param trainerEntity {@code TrainerEntity} to be added to storage
     */
    public void createTrainer(TrainerEntity trainerEntity) {
        Long id = generateId();
        trainerEntity.setUserId(id);
        dataStorage.getTrainerStorage().put(id, trainerEntity);
        dataStorage.getTrainerStorageUsernameKey().put(trainerEntity.getUsername(), trainerEntity);
    }

    /**
     * Generates a unique id for the trainer entity.
     */
    public Long generateId() {
        OptionalLong lastId = dataStorage.getTrainerStorage()
                .values().stream().mapToLong(TrainerEntity::getUserId).max();
        if (lastId.isPresent()) {
            return lastId.getAsLong() + 1;
        } else {
            return 0L;
        }
    }

    /**
     * Gets trainer by username.
     *
     * @param username username of the trainer
     * @return {@code Optional<TrainerEntity>}
     */
    public Optional<TrainerEntity> getTrainerByUsername(String username) {
        return Optional.ofNullable(dataStorage.getTrainerStorageUsernameKey().get(username));
    }

    /**
     * Gets trainer by id.
     *
     * @param id id of the trainer
     * @return {@code Optional<TrainerEntity>}
     */
    public Optional<TrainerEntity> getTrainerById(Long id) {
        return Optional.ofNullable(dataStorage.getTrainerStorage().get(id));
    }

    /**
     * Updates trainer entity in storage by id.
     * If no trainer is found throws an {@code IllegalIdException}
     *
     * @param id id of the trainer to be updated
     * @param trainerEntity new {@code TrainerEntity} to update with
     */
    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
        if (!dataStorage.getTrainerStorage().containsKey(id)) {
            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
        }
        dataStorage.getTrainerStorage().put(id, trainerEntity);
        dataStorage.getTrainerStorageUsernameKey().put(trainerEntity.getUsername(), trainerEntity);
    }

}
