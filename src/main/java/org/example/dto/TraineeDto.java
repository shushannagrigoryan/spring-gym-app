package org.example.dto;

import lombok.*;

import java.time.LocalDate;
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
