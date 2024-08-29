package org.example.model;

import java.time.LocalDate;

public class Trainee extends User{
    private final LocalDate dateOfBirth;
    private final String address;

    public Trainee(String firstName, String lastName,
                   LocalDate dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;

    }

    @Override
    public String toString() {
        return "Trainee{" +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                '}';
    }



    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName){
        this.username = userName;
    }
}
