package org.example.entity;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraineeEntity extends UserEntity {
    private LocalDate dateOfBirth;
    private String address;

    /**
     * Constructs a new instance of {@code TraineeEntity}.
     *
     * @param firstname   The firstname of the Trainee.
     * @param lastName    The lastname of the Trainee.
     * @param password    The password of the Trainee.
     * @param dateOfBirth The dateOfBirth of the Trainee.
     * @param address     The address of the Trainee.
     */
    public TraineeEntity(String firstname, String lastName,
                         String password, LocalDate dateOfBirth,
                         String address) {
        this.firstName = firstname;
        this.lastName = lastName;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    @Override
    public String toString() {
        return "TraineeEntity{"
                + "dateOfBirth=" + dateOfBirth
                + ", address='" + address + '\''
                + ", userId=" + userId
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", username='" + username + '\''
                + ", password='" + password + '\''
                + ", isActive=" + isActive + '}';
    }
}