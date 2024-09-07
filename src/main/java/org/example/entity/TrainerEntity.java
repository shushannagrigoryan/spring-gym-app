package org.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TrainerEntity extends UserEntity {
    private String specialization;

    /**
     * Constructs a new instance of {@code TrainerEntity}.
     *
     * @param firstName      The firstname of the Trainer.
     * @param lastName       The lastname of the Trainer.
     * @param password       The password of the Trainer.
     * @param specialization The specialization of the Trainer.
     */
    public TrainerEntity(String firstName, String lastName,
                         String password, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "TrainerEntity{"
                + "specialization='" + specialization + '\''
                + ", userId=" + userId
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", username='" + username + '\''
                + ", password='" + password + '\''
                + ", isActive=" + isActive + '}';
    }
}
