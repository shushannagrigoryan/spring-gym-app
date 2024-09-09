package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainerMapper {

    /**
     * Maps a {@code TrainerEntity} to a {@code TrainerDto}.
     */
    public TrainerDto entityToDto(TrainerEntity trainerEntity) {
        log.info("Mapping TrainerEntity {} to TrainerDto", trainerEntity);
        if (trainerEntity == null) {
            return null;
        }

        return new TrainerDto(trainerEntity.getFirstName(), trainerEntity.getLastName(),
                trainerEntity.getPassword(), trainerEntity.getSpecialization());
    }

    /**
     * Maps a {@code TrainerDto} to a {@code TrainerEntity}.
     */
    public TrainerEntity dtoToEntity(TrainerDto trainerDto) {
        log.info("Mapping TrainerDto {} to TrainerEntity", trainerDto);
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
