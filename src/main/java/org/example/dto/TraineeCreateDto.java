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
public class TraineeCreateDto {
    @NotNull(message = "FirstName is required.")
    private String firstName;
    @NotNull(message = "LastName is required.")
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
