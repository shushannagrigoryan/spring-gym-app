package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class TraineeDto {
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;
    private String address;

    public TraineeDto(String firstName, String lastName, String password, LocalDate dateOfBirth, String address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

}
