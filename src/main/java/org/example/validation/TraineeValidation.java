package org.example.validation;

import org.example.dto.TraineeDto;
import org.example.exceptions.GymIllegalArgumentException;
import org.springframework.stereotype.Component;

@Component
public class TraineeValidation {
    /** Validates {@code TraineeDto} for required fields. */
    public void validateTrainee(TraineeDto traineeDto) {
        if (traineeDto.getFirstName() == null || traineeDto.getFirstName().trim().isEmpty()) {
            throw new GymIllegalArgumentException("Trainee firstName is required.");
        }
        if (traineeDto.getLastName() == null || traineeDto.getLastName().trim().isEmpty()) {
            throw new GymIllegalArgumentException("Trainee lastName is required.");
        }
        if (traineeDto.getDateOfBirth() == null) {
            throw new GymIllegalArgumentException("Trainee dateOfBirth is required.");
        }
        if (traineeDto.getAddress() == null || traineeDto.getAddress().trim().isEmpty()) {
            throw new GymIllegalArgumentException("Address  is required.");
        }
    }
}
