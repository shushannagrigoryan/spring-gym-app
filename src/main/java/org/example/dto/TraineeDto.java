package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Getter
@Setter
@ToString
@AllArgsConstructor
public class TraineeDto {
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;
    private String address;

}
