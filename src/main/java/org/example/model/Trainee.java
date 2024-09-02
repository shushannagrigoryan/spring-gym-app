package org.example.model;

import java.time.LocalDate;


public class Trainee extends User{
    private  LocalDate dateOfBirth;
    private  String address;

//    public Trainee(Long userId,String firstName, String lastName, String username, String password,
//                   boolean isActive, LocalDate dateOfBirth, String address){
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.username = username;
//        this.password = password;
//        this.isActive = isActive;
//        this.dateOfBirth = dateOfBirth;
//        this.address = address;
//        this.userId = userId;
//    }



    public Trainee(String firstName, String lastName,
                   LocalDate dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(){

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


    public void setId(Long id){
        this.userId = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public Long getUserId(){
        return this.userId;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public boolean getIsActive(){
        return this.isActive;
    }



}
