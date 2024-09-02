package org.example.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainerEntity extends UserEntity{
    private  String specialization;

    public TrainerEntity(){

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
