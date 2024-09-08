package org.example.mapper;

import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapper {
    /**
     * Maps a {@code TraineeEntity} to a {@code TraineeDto}.
     */
    public TraineeDto entityToDto(TraineeEntity traineeEntity) {
        if (traineeEntity == null) {
            return null;
        }

        return new TraineeDto(traineeEntity.getFirstName(), traineeEntity.getLastName(),
                traineeEntity.getPassword(), traineeEntity.getDateOfBirth(), traineeEntity.getAddress());
    }

    /**
     * Maps a {@code TraineeDto} to {@code TraineeEntity}.
     */
    public TraineeEntity dtoToEntity(TraineeDto traineeDto) {
        if (traineeDto == null) {
            return null;
        }

        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setFirstName(traineeDto.getFirstName());
        traineeEntity.setLastName(traineeDto.getLastName());
        traineeEntity.setDateOfBirth(traineeDto.getDateOfBirth());
        traineeEntity.setAddress(traineeDto.getAddress());
        traineeEntity.setPassword(traineeDto.getPassword());

        return traineeEntity;
    }
}
