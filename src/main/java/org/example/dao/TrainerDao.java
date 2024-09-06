package org.example.dao;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalIdException;
import org.springframework.stereotype.Component;

@Component
public class TrainerDao {
    private final Map<Long, TrainerEntity> trainerStorage;

    public TrainerDao(Map<Long, TrainerEntity> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    /** Generates an id for the Trainer entity and adds the entity to the storage map. */
    public void createTrainer(TrainerEntity trainerEntity) {
        Long id = generateId();
        trainerEntity.setUserId(id);
        trainerStorage.put(id, trainerEntity);
    }

    /** Generates a unique id for the Trainer entity.*/
    public Long generateId() {
        OptionalLong lastId = trainerStorage.values().stream().mapToLong(TrainerEntity::getUserId).max();
        if (lastId.isPresent()) {
            return lastId.getAsLong() + 1;
        } else {
            return 0L;
        }
    }

    public Optional<TrainerEntity> getTrainerByUsername(String username) {
        return trainerStorage.values().stream().filter(elem -> elem.getUsername().equals(username)).findFirst();
    }

    public Optional<TrainerEntity> getTrainerById(Long id) {
        return Optional.ofNullable(trainerStorage.get(id));
    }

    /** Updates Trainer entity in storage by id. If no trainer is found throws a IllegalIdException */
    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
        if (!trainerStorage.containsKey(id)) {
            throw new IllegalIdException("No trainer with id: " + id);
        }
        trainerStorage.put(id, trainerEntity);
    }

}
