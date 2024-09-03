package org.example.dao;

import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalIdException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

@Component
public class TrainerDao {
    private final Map<Long, TrainerEntity> trainerStorage;

    public TrainerDao(Map<Long, TrainerEntity> trainerStorage){
        this.trainerStorage = trainerStorage;
    }
    public void createTrainer(TrainerEntity trainerEntity){
        Long id = generateId();
        trainerEntity.setUserId(id);
        trainerStorage.put(id,trainerEntity);
    }

    private Long generateId(){
        OptionalLong lastId = trainerStorage.values().stream()
                .mapToLong(TrainerEntity::getUserId)
                .max();
        if(lastId.isPresent()){
            return lastId.getAsLong() + 1;
        }
        else{
            return 0L;
        }
    }

    public Optional<TrainerEntity> getTrainerByUsername(String username){
        return trainerStorage.values().stream()
                .filter(elem -> elem.getUsername().equals(username))
                .findFirst();
    }

    public Optional<TrainerEntity> getTrainerById(Long id){
        return Optional.ofNullable(trainerStorage.get(id));
    }

    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
        if (!trainerStorage.containsKey(id)){
            throw new IllegalIdException("No trainee with id: " + id);
        }
        trainerStorage.put(id, trainerEntity);
    }

}
