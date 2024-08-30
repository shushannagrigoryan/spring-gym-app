package org.example.model;

public class Trainer extends User{
    private final String specialization;

    public Trainer(String specialization) {
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
}
