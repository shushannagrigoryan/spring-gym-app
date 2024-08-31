package org.example.model;

import java.time.LocalDate;

public class Trainer extends User{
    private final String specialization;

    public Trainer(String firstName, String lastName, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName){
        this.username = userName;
    }

    public void setId(Long id){
        this.userId = id;
    }

    public long getUserID() {
        return this.userId;
    }
}
