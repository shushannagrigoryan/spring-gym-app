package org.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TraineeEntity extends UserEntity {
    private LocalDate dateOfBirth;
    private String address;

    public TraineeEntity(String firstname, String lastName,
                         String password, LocalDate dateOfBirth,
                         String address){
        this.firstName = firstname;
        this.lastName = lastName;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}