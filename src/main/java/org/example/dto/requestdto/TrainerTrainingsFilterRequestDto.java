package org.example.dto.requestdto;

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
public class TrainerTrainingsFilterRequestDto {
    @CustomDateTimeConstraint
    private String fromDate;
    @CustomDateTimeConstraint
    private String toDate;
    private String traineeUsername;
}
