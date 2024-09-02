package org.example.mapper;

import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {
    public TrainerDto entityToDto(TrainerEntity trainerEntity) {
        if (trainerEntity == null) {
            return null;
        }

        return new TrainerDto(trainerEntity.getFirstName(), trainerEntity.getLastName(),
                trainerEntity.getPassword(), trainerEntity.getSpecialization());
    }

    public TrainerEntity dtoToEntity(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }

        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setFirstName(trainerDto.getFirstName());
        trainerEntity.setLastName(trainerDto.getLastName());
        trainerEntity.setPassword(trainerDto.getPassword());
        trainerEntity.setSpecialization(trainerDto.getSpecialization());

        return trainerEntity;
    }
}
