package org.example.dao;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.storage.DataStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeDao {
    @Autowired
    private DataStorage dataStorage;
    private IdGenerator idGenerator;

    @Autowired
    public void setDependencies(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * Generates id for the trainee entity and adds that entity to the storage map.
     *
     * @param traineeEntity {@code TraineeEntity} to be added to storage
     */
    public void createTrainee(TraineeEntity traineeEntity) {
        Long id = idGenerator.generateId("Trainee");
        traineeEntity.setUserId(id);
        log.debug("Saving trainee: {} to storage", traineeEntity);
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
        log.debug("Getting trainee with username: {}", username);
        return Optional.ofNullable(dataStorage.getTraineeStorageUsernameKey().get(username));
    }

    /**
     * Gets trainee by id.
     *
     * @param id id of the trainee
     * @return {@code Optional<TraineeEntity>}
     */
    public Optional<TraineeEntity> getTraineeById(Long id) {
        log.debug("Getting trainee with id: {}", id);
        return Optional.ofNullable(dataStorage.getTraineeStorage().get(id));
    }

    /**
     * Deletes the trainee entity from the storage by id.
     * If no trainee is found throws an {@code IllegalIdException}.
     *
     * @param id id of the trainee to be deleted
     */
    public void deleteTraineeById(Long id) {
        if (dataStorage.getTraineeStorage().containsKey(id)) {
            log.debug("Deleting trainee with id: {} from storage", id);
            dataStorage.getTraineeStorage().remove(id);
        } else {
            log.debug("No trainee with id: {}", id);
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
            log.debug("No trainee with id: {}", id);
            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
        }
        log.debug("Updating trainee with id: {} with {}", id, traineeEntity);
        dataStorage.getTraineeStorage().put(id, traineeEntity);
        dataStorage.getTraineeStorageUsernameKey().put(traineeEntity.getUsername(), traineeEntity);
    }
}
