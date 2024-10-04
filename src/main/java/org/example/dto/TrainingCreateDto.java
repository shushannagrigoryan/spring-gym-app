package org.example.dto;

import java.math.BigDecimal;
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
public class TrainingCreateDto {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private Long trainingType;
    private LocalDate trainingDate;
    private BigDecimal trainingDuration;

}
