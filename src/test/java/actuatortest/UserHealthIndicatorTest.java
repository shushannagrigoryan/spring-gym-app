package actuatortest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.actuator.UserHealthIndicator;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.repositories.TraineeRepository;
import org.example.repositories.TrainerRepository;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;

@ExtendWith(MockitoExtension.class)
public class UserHealthIndicatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private UserHealthIndicator userHealthIndicator;


    @Test
    public void testUserHealthIndicator() {
        //given
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user1);
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user2);
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        //when
        Health health = userHealthIndicator.health();

        //then
        assertEquals(Health.up().withDetail("User Existence Health Indicator", "All users are valid").build(), health);
    }

    @Test
    public void testUserHealthIndicatorInvalidTrainee() {
        //given
        UserEntity user = new UserEntity();
        user.setId(1L);
        TraineeEntity trainee1 = new TraineeEntity();
        trainee1.setUser(user);
        TraineeEntity trainee2 = new TraineeEntity();
        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        trainee2.setUser(user2);
        when(traineeRepository.findAll()).thenReturn(List.of(trainee1, trainee2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(trainerRepository.findAll()).thenReturn(List.of());

        //when
        Health health = userHealthIndicator.health();

        //then
        assertEquals(Health.down().withDetail("User Existence Health Indicator",
            "Found trainees or trainers without corresponding users").build(), health);
    }

    @Test
    public void testUserHealthIndicatorInvalidTrainer() {
        //given
        UserEntity user = new UserEntity();
        user.setId(1L);
        TrainerEntity trainer1 = new TrainerEntity();
        trainer1.setUser(user);
        TrainerEntity trainer2 = new TrainerEntity();
        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        trainer2.setUser(user2);
        when(traineeRepository.findAll()).thenReturn(List.of());
        when(trainerRepository.findAll()).thenReturn(List.of(trainer1, trainer2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        //when
        Health health = userHealthIndicator.health();

        //then
        assertEquals(Health.down().withDetail("User Existence Health Indicator",
            "Found trainees or trainers without corresponding users").build(), health);
    }

}

