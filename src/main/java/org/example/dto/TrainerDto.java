package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@AllArgsConstructor
public class TrainerDto {
    private String firstName;
    private String lastName;
    private String password;
    private String specialization;

}
