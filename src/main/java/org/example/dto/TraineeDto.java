package org.example.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDto {
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;
    private String address;

}
