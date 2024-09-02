package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Getter
@Setter
@ToString
public class TrainerDto {
    private String firstName;
    private String lastName;
    private String password;
    private String specialization;

    public TrainerDto(String firstName, String lastName, String password, String specialization){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.specialization = specialization;
    }
}
