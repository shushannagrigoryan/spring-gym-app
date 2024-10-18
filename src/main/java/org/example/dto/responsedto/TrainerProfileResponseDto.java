package org.example.dto.responsedto;

import java.util.Set;
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
public class TrainerProfileResponseDto {
    private String firstName;
    private String lastName;
    private TrainingTypeResponseDto specialization;
    private boolean isActive;
    private Set<TrainerProfileTraineeResponseDto> trainees;

}
