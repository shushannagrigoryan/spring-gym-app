package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.UserDao;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
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

        return new TraineeDto(traineeEntity.getUser().getFirstName(),
                traineeEntity.getUser().getLastName(),
                traineeEntity.getDateOfBirth(), traineeEntity.getAddress());
    }

    /**
     * Maps a {@code TraineeDto} to {@code TraineeEntity}.
     */
    public TraineeEntity dtoToEntity(TraineeDto traineeDto) {
        log.info("Mapping TraineeDto {} to TraineeEntity", traineeDto);
        System.out.println("traineeDto = " + traineeDto);
        if (traineeDto == null) {
            return null;
        }

        TraineeEntity traineeEntity = new TraineeEntity();
        //UserEntity user = new UserEntity();
        //traineeEntity.setUser(user);
        System.out.println("traineeEntity.getUser() = " + traineeEntity.getUser());
        //        traineeEntity.setUser(new UserEntity(traineeDto.getFirstName(), traineeDto.getLastName(),
        //                                "username", "password"));
        traineeEntity.setUser(new UserEntity(traineeDto.getFirstName(), traineeDto.getLastName()));
        System.out.println("traineeEntity = " + traineeEntity);
        //traineeEntity.getUser().setFirstName(traineeDto.getFirstName());
        //traineeEntity.getUser().setLastName(traineeDto.getLastName());
        traineeEntity.setDateOfBirth(traineeDto.getDateOfBirth());
        traineeEntity.setAddress(traineeDto.getAddress());

        return traineeEntity;
    }
}
