package org.example.dao;

import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

@Component
public class TrainingDao {
    private final Map<Long, TrainingEntity> trainingStorage;

    public TrainingDao(Map<Long, TrainingEntity> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public void createTraining(TrainingEntity trainingEntity){
        Long id = generateId();
        trainingEntity.setTrainingId(id);
        trainingStorage.put(id,trainingEntity);
    }

    public Long generateId(){
        OptionalLong lastId = trainingStorage.values().stream()
                .mapToLong(TrainingEntity::getTrainingId)
                .max();
        if(lastId.isPresent()){
            return lastId.getAsLong() + 1;
        }
        else{
            return 0L;
        }
    }

    public Optional<TrainingEntity> getTrainingById(Long id){
        return Optional.ofNullable(trainingStorage.get(id));
    }
}
