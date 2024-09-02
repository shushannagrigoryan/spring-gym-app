package org.example.model;

import java.time.LocalDate;

public class Trainer extends User{
    private  String specialization;

    public Trainer(String firstName, String lastName, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
    }

    public Trainer(){

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

    public long getUserId() {
        return this.userId;
    }

    public String getSpecialization() {
        return specialization;
    }
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean getIsActive(){
        return this.isActive;
    }






}
