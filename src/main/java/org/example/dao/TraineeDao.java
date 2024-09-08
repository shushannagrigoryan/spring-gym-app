package org.example.dao;

import java.util.Optional;
import java.util.OptionalLong;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.storage.DataStorage;
import org.springframework.stereotype.Component;

@Component
public class TraineeDao {
    private final DataStorage dataStorage;

    public TraineeDao(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Generates id for the trainee entity and adds that entity to the storage map.
     *
     * @param traineeEntity {@code TraineeEntity} to be added to storage
     */
    public void createTrainee(TraineeEntity traineeEntity) {
        Long id = generateId();
        traineeEntity.setUserId(id);
        dataStorage.getTraineeStorage().put(id, traineeEntity);
        dataStorage.getTraineeStorageUsernameKey().put(traineeEntity.getUsername(), traineeEntity);
    }

    /**
     * Gets trainee by username.
     *
     * @param username username of the trainee
     * @return {@code Optional<TraineeEntity>}
     */
    public Optional<TraineeEntity> getTraineeByUsername(String username) {
        return Optional.ofNullable(dataStorage.getTraineeStorageUsernameKey().get(username));
    }

    /**
     * Gets trainee by id.
     *
     * @param id id of the trainee
     * @return {@code Optional<TraineeEntity>}
     */
    public Optional<TraineeEntity> getTraineeById(Long id) {
        return Optional.ofNullable(dataStorage.getTraineeStorage().get(id));
    }

    /** Generates a unique id for the trainee entity. */
    public Long generateId() {
        OptionalLong lastId = dataStorage.getTraineeStorage()
                .values().stream().mapToLong(TraineeEntity::getUserId).max();
        if (lastId.isPresent()) {
            return lastId.getAsLong() + 1;
        } else {
            return 0L;
        }
    }

    /**
     * Deletes the trainee entity from the storage by id.
     * If no trainee is found throws an {@code IllegalIdException}.
     *
     * @param id id of the trainee to be deleted
     */
    public void deleteTraineeById(Long id) {
        if (dataStorage.getTraineeStorage().containsKey(id)) {
            dataStorage.getTraineeStorage().remove(id);
        } else {
            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
        }
    }

    /**
     * Updates trainee entity in storage by id.
     * If no trainee is found throws an {@code IllegalIdException}
     *
     * @param id id of the trainee to be updated
     * @param traineeEntity new {@code TraineeEntity} to update with
     */
    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        if (!dataStorage.getTraineeStorage().containsKey(id)) {
            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
        }
        dataStorage.getTraineeStorage().put(id, traineeEntity);
        dataStorage.getTraineeStorageUsernameKey().put(traineeEntity.getUsername(), traineeEntity);
    }
}
