package org.example.dao;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.springframework.stereotype.Component;

@Component
public class TraineeDao {
    private final Map<Long, TraineeEntity> traineeStorage;

    public TraineeDao(Map<Long, TraineeEntity> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    /** Generates id for the trainee entity and adds that entity to the storage map.*/
    public void createTrainee(TraineeEntity traineeEntity) {
        Long id = generateId();
        traineeEntity.setUserId(id);
        traineeStorage.put(id, traineeEntity);
    }

    public Optional<TraineeEntity> getTraineeByUsername(String username) {
        return traineeStorage.values().stream().filter(elem -> elem.getUsername().equals(username)).findFirst();
    }

    public Optional<TraineeEntity> getTraineeById(Long id) {
        return Optional.ofNullable(traineeStorage.get(id));
    }

    /** Generates a unique id for the Trainee entity.*/
    public Long generateId() {
        OptionalLong lastId = traineeStorage.values().stream().mapToLong(TraineeEntity::getUserId).max();
        if (lastId.isPresent()) {
            return lastId.getAsLong() + 1;
        } else {
            return 0L;
        }
    }

    /** Deletes the Trainee entity from the storage by id. If no trainee was found throws an IllegalIdException. */
    public void deleteTraineeById(Long id) {
        if (traineeStorage.containsKey(id)) {
            traineeStorage.remove(id);
        } else {
            throw new IllegalIdException("No trainee with id: " + id);
        }
    }

    /** Updates Trainee entity in storage by id. If no trainee is found throws a IllegalIdException */
    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        if (!traineeStorage.containsKey(id)) {
            throw new IllegalIdException("No trainee with id: " + id);
        }
        traineeStorage.put(id, traineeEntity);
    }
}
