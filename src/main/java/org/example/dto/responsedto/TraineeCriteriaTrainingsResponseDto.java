package org.example.dto.responsedto;

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
public class TraineeCriteriaTrainingsResponseDto {
    private String trainingName;
    private LocalDate trainingDate;
    private TrainingTypeResponseDto trainingType;
    private BigDecimal trainingDuration;
    private String trainerName;
}
