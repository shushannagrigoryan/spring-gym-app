package org.example.dto.requestdto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
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
public class TraineeUpdateTrainersRequestDto {
    @NotNull(message = "Trainee username is required.")
    private String username;
    @NotNull(message = "Trainer List is required.")
    private List<String> trainers;
}
