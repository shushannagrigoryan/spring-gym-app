package org.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TrainerEntity extends UserEntity{
    private  String specialization;
    public TrainerEntity(String firstName, String lastName,
                         String password, String specialization){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specialization='" + specialization + '\'' +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
