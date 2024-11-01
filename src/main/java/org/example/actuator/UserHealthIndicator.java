package org.example.actuator;

import java.util.Optional;
import org.example.repositories.TraineeRepository;
import org.example.repositories.TrainerRepository;
import org.example.repositories.UserRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class UserHealthIndicator implements HealthIndicator {
    private final UserRepository userRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    /** Setting dependencies. */
    public UserHealthIndicator(UserRepository userRepository,
                               TraineeRepository traineeRepository,
                               TrainerRepository trainerRepository) {
        this.userRepository = userRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    /**
     * User existence health indicator.
     * Checks whether all trainees/trainers have corresponding users.
     *
     * @return {@code Health}
     */
    @Override
    public Health health() {
        boolean invalidTrainee =
                traineeRepository.findAll().stream()
                                .map(x -> userRepository.findById(x.getUser().getId()))
                        .anyMatch(Optional::isEmpty);

        boolean invalidTrainer =
                trainerRepository.findAll().stream()
                        .map(x -> userRepository.findById(x.getUser().getId()))
                        .anyMatch(Optional::isEmpty);

        if (invalidTrainee || invalidTrainer) {
            return Health.down().withDetail("User Existence Health Indicator",
                    "Found trainees or trainers without corresponding users").build();
        }

        return Health.up().withDetail("User Existence Health Indicator", "All users are valid").build();
    }

}
