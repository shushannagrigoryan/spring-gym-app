package org.example.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeEntity extends UserEntity {
    private LocalDate dateOfBirth;
    private String address;

    /**
     * Constructs a new instance of {@code TraineeEntity}.
     *
     * @param firstname   The firstname of the trainee.
     * @param lastName    The lastname of the trainee.
     * @param dateOfBirth The dateOfBirth of the trainee.
     * @param address     The address of the trainee.
     */
    public TraineeEntity(String firstname, String lastName,
                         LocalDate dateOfBirth, String address) {
        this.firstName = firstname;
        this.lastName = lastName;
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