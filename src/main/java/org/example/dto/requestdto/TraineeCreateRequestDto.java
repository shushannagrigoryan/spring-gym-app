package org.example.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "FirstName is required and can't be blank")
    private String firstName;
    @NotBlank(message = "LastName is required and can't be blank")
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
