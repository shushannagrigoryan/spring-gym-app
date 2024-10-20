package org.example.dto.requestdto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TrainerCreateRequestDto {
    @NotBlank(message = "FirstName is required and can't be blank")
    private String firstName;
    @NotBlank(message = "LastName is required and can't be blank")
    private String lastName;
    @NotNull(message = "Specialization is required.")
    @Min(value = 1, message = "Specialization must be greater than or equal to 1")
    @Digits(message = "Specialization must be a valid long number.", integer = 50, fraction = 0)
    private String specialization;
}
