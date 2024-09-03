package org.example.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDto {
    private String firstName;
    private String lastName;
    private String password;
    private String specialization;

}
