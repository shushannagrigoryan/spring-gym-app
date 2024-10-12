package org.example.dto.requestdto;

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
public class TraineeCreateRequestDto {
    @NotNull(message = "FirstName is required.")
    private String firstName;
    @NotNull(message = "LastName is required.")
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
