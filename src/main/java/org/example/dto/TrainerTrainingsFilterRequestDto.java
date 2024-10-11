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
public class TrainerTrainingsFilterRequestDto {
    @NotNull(message = "Trainer Username is required.")
    private String trainerUsername;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String traineeUsername;
}
