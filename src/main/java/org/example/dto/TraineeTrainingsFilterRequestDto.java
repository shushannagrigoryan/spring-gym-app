package org.example.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingsFilterRequestDto {
    @NotNull(message = "Trainee Username is required.")
    private String traineeUsername;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String trainerUsername;
    private Long trainingType;

}
