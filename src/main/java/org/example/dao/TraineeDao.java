package org.example.dao;

import java.util.Optional;
import java.util.OptionalLong;
import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.example.storage.DataStorage;
import org.springframework.stereotype.Component;

@Component
public class TraineeDao {
    private final DataStorage dataStorage;

    public TraineeDao(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /** Generates id for the trainee entity and adds that entity to the storage map.*/
    public void createTrainee(TraineeEntity traineeEntity) {
        Long id = generateId();
        traineeEntity.setUserId(id);
        dataStorage.getTraineeStorage().put(id, traineeEntity);
    }

    public Optional<TraineeEntity> getTraineeByUsername(String username) {
        return dataStorage.getTraineeStorage().values()
                .stream().filter(elem -> elem.getUsername().equals(username)).findFirst();
    }

    public Optional<TraineeEntity> getTraineeById(Long id) {
        return Optional.ofNullable(dataStorage.getTraineeStorage().get(id));
    }

    /** Generates a unique id for the Trainee entity.*/
    public Long generateId() {
        OptionalLong lastId = dataStorage.getTraineeStorage()
                .values().stream().mapToLong(TraineeEntity::getUserId).max();
        if (lastId.isPresent()) {
            return lastId.getAsLong() + 1;
        } else {
            return 0L;
        }
    }

    /** Deletes the Trainee entity from the storage by id. If no trainee was found throws an IllegalIdException. */
    public void deleteTraineeById(Long id) {
        if (dataStorage.getTraineeStorage().containsKey(id)) {
            dataStorage.getTraineeStorage().remove(id);
        } else {
            throw new IllegalIdException("No trainee with id: " + id);
        }
    }

    /** Updates Trainee entity in storage by id. If no trainee is found throws a IllegalIdException */
    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        if (!dataStorage.getTraineeStorage().containsKey(id)) {
            throw new IllegalIdException("No trainee with id: " + id);
        }
        dataStorage.getTraineeStorage().put(id, traineeEntity);
    }
}
