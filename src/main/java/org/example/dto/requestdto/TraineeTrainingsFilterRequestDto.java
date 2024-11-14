package org.example.dto.requestdto;

import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.constraints.CustomDateTimeConstraint;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingsFilterRequestDto {
    @CustomDateTimeConstraint
    private String fromDate;
    @CustomDateTimeConstraint
    private String toDate;
    private String trainerUsername;
    @Digits(message = "Training type must be a valid long number.", integer = 50, fraction = 0)
    private String trainingType;

}
