package org.example.dao;

import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

@Component
public class TraineeDao {
    private final Map<Long, TraineeEntity> traineeStorage;
    public TraineeDao(Map<Long, TraineeEntity> traineeStorage){
        this.traineeStorage = traineeStorage;
    }
    public void createTrainee(TraineeEntity traineeEntity){
        Long id = generateId();
        traineeEntity.setUserId(id);
        traineeStorage.put(id,traineeEntity);
    }

    public Optional<TraineeEntity> getTraineeByUsername(String username){
        return traineeStorage.values().stream()
                .filter(elem -> elem.getUsername().equals(username))
                .findFirst();
    }

    public Optional<TraineeEntity> getTraineeById(Long id){
        return Optional.ofNullable(traineeStorage.get(id));
    }

    private Long generateId(){
        OptionalLong lastId = traineeStorage.values().stream()
                .mapToLong(TraineeEntity::getUserId)
                .max();
        if(lastId.isPresent()){
            return lastId.getAsLong() + 1;
        }
        else{
            return 0L;
        }
    }

    public void deleteTraineeById(Long id) {
        if(traineeStorage.containsKey(id)){
            traineeStorage.remove(id);
        }else{
            throw new IllegalIdException(id);
        }
    }

    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        if (!traineeStorage.containsKey(id)){
            throw new IllegalIdException(id);
        }
        traineeStorage.put(id, traineeEntity);
    }
}
