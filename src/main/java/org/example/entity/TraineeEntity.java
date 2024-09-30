package org.example.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// I think that the entity classes should also contain the toString method for proper logging.
// I would also add equals and hashCode, because, in my opinion, they may be useful both for testing purposes
// and in the future, when new functionality is added.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainees")
public class TraineeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateOfBirth;

    private String address;

    @OneToOne
    private UserEntity user;

    @OneToMany(mappedBy = "trainee", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TrainingEntity> trainings;


}
