package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TraineeMapper {
    /**
     * Maps a {@code TraineeEntity} to a {@code TraineeDto}.
     */
    public TraineeDto entityToDto(TraineeEntity traineeEntity) {
        log.info("Mapping TraineeEntity {} to TraineeDto", traineeEntity);
        if (traineeEntity == null) {
            return null;
        }

        return new TraineeDto(traineeEntity.getFirstName(), traineeEntity.getLastName(),
                traineeEntity.getDateOfBirth(), traineeEntity.getAddress());
    }

    /**
     * Maps a {@code TraineeDto} to {@code TraineeEntity}.
     */
    public TraineeEntity dtoToEntity(TraineeDto traineeDto) {
        log.info("Mapping TraineeDto {} to TraineeEntity", traineeDto);
        if (traineeDto == null) {
            return null;
        }

        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setFirstName(traineeDto.getFirstName());
        traineeEntity.setLastName(traineeDto.getLastName());
        traineeEntity.setDateOfBirth(traineeDto.getDateOfBirth());
        traineeEntity.setAddress(traineeDto.getAddress());

        return traineeEntity;
    }
}
