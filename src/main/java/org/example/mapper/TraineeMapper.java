package org.example.mapper;

import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapper {
    public TraineeDto entityToDto(TraineeEntity trainee) {
        if (trainee == null) {
            return null;
        }

        //        traineeDto.setFirstName(trainee.getFirstName());
//        traineeDto.setLastName(trainee.getLastName());
//        traineeDto.setPassword(trainee.getPassword());
//        traineeDto.setDateOfBirth(trainee.getDateOfBirth());
//        traineeDto.setAddress(trainee.getAddress());

        return new TraineeDto(trainee.getFirstName(), trainee.getLastName(),
                trainee.getPassword(), trainee.getDateOfBirth(), trainee.getAddress());
    }

    public TraineeEntity dtoToEntity(TraineeDto traineeDto) {
        if (traineeDto == null) {
            return null;
        }

        TraineeEntity trainee = new TraineeEntity();
        trainee.setFirstName(traineeDto.getFirstName());
        trainee.setLastName(traineeDto.getLastName());
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());
        trainee.setAddress(traineeDto.getAddress());
        trainee.setPassword(traineeDto.getPassword());

        return trainee;
    }
}
