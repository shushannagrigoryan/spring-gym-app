package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.password.PasswordGeneration;
import org.example.repository.TraineeRepository;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.username.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private UsernameGenerator usernameGenerator;
    @Mock
    private PasswordGeneration passwordGeneration;
    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    public void testRegisterTraineeSuccess() {
        //given
        String password = "myPassword";
        TraineeEntity traineeEntity = new TraineeEntity();
        UserEntity user = new UserEntity();
        traineeEntity.setUser(user);
        traineeEntity.getUser().setPassword(password);
        when(usernameGenerator.generateUsername(traineeEntity.getUser().getFirstName(),
                traineeEntity.getUser().getLastName()))
                .thenReturn("Jack.Jones");
        when(traineeRepository.save(traineeEntity)).thenReturn(traineeEntity);

        //when
        TraineeEntity result = traineeService.registerTrainee(traineeEntity);

        //then
        verify(passwordGeneration).generatePassword();
        verify(usernameGenerator).generateUsername(traineeEntity.getUser().getFirstName(),
                traineeEntity.getUser().getLastName());
        assertEquals("Jack.Jones", result.getUser().getUsername());

    }


    @Test
    public void testGetTraineeByUsernameSuccess() {
        //given
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String address = "myAddress";
        TraineeEntity traineeEntity = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        traineeEntity.setUser(user);
        traineeEntity.setDateOfBirth(LocalDate.now());
        traineeEntity.setAddress(address);
        String username = firstName.concat(".").concat(lastName);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(traineeEntity));

        //when
        traineeService.getTraineeByUsername(username);

        //then
        verify(traineeRepository).findByUsername(username);
    }

    @Test
    public void testGetTraineeByUsernameFailure() {
        //given
        String username = "John.Smith";

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        //then
        assertThrows(GymEntityNotFoundException.class,
                () -> traineeService.getTraineeByUsername(username),
                String.format("Trainee with username %s does not exist.", username));
        verify(traineeRepository).findByUsername(username);
    }

    @Test
    public void testGetTraineeByIdSuccess() {
        //given
        TraineeEntity traineeEntity = new TraineeEntity();
        Long id = 1L;
        traineeEntity.setId(id);
        when(traineeRepository.findById(id)).thenReturn(Optional.of(traineeEntity));

        //when
        TraineeEntity trainee = traineeService.getTraineeById(id);

        //then
        verify(traineeRepository).findById(id);
        assertNotNull(trainee);
    }

    @Test
    public void testGetTraineeBIdFailure() {
        //given
        Long id = 1L;

        when(traineeRepository.findById(id)).thenReturn(Optional.empty());

        //then
        assertThrows(GymEntityNotFoundException.class,
                () -> traineeService.getTraineeById(id),
                String.format("No trainee with id: %d", id));
        verify(traineeRepository).findById(id);
    }


    @Test
    public void testDeleteTraineeByUsernameSuccess() {
        //given
        String username = "A.A";
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).delete(trainee);

        //when
        traineeService.deleteTraineeByUsername(username);

        //then
        verify(traineeRepository).delete(trainee);
    }

    @Test
    public void testDeleteTraineeByUsernameFailure() {
        //given
        String username = "A.A";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        //then
        assertThrows(GymEntityNotFoundException.class,
                () -> traineeService.deleteTraineeByUsername(username),
                String.format(String.format("Trainee with username: %s does not exist.", username)));
    }


    @Test
    public void testUpdateTraineeByIdInvalidUsername() {
        //given
        String username = "A.A";
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        trainee.setUser(user);
        traineeRepository.findByUsername(username);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        //then
        RuntimeException exception =
                assertThrows(GymIllegalArgumentException.class,
                        () -> traineeService.updateTrainee(trainee));
        assertEquals(String.format("No trainee with username: %s", username), exception.getMessage());
    }

    @Test
    public void testUpdateTraineeSuccess() {
        //given
        String firstName = "A";
        String lastName = "V";
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setDateOfBirth(LocalDate.now());
        traineeEntity.setAddress("myAddress");
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername("A.B");
        traineeEntity.setUser(user);
        when(traineeRepository.findByUsername("A.B")).thenReturn(Optional.of(traineeEntity));
        user.setFirstName("B");
        when(traineeRepository.save(traineeEntity)).thenReturn(traineeEntity);

        // when
        TraineeEntity trainee =  traineeService.updateTrainee(traineeEntity);

        // then
        assertNotNull(trainee);
        assertEquals("B", trainee.getUser().getFirstName());
        assertEquals("A.B", trainee.getUser().getUsername());
    }

    @Test
    public void testActivateTraineeSuccess() {
        //given
        String username = "A.I";
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        trainee.setUser(user);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        TraineeEntity activatedTrainee = new TraineeEntity();
        activatedTrainee.setUser(user);
        activatedTrainee.getUser().setActive(true);
        when(traineeRepository.save(trainee)).thenReturn(activatedTrainee);

        //when
        String result = traineeService.changeActiveStatus(username, false);

        //then
        verify(traineeRepository).findByUsername(username);
        verify(traineeRepository).save(trainee);
        assertEquals("Successfully set trainee active status to false", result);
    }

    @Test
    public void testActivateTraineeAlreadyActive() {
        //given
        String username = "A.I";
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setActive(true);
        user.setUsername(username);
        trainee.setUser(user);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        //when
        String result = traineeService.changeActiveStatus(username, true);

        //then
        verify(traineeRepository).findByUsername(username);
        assertEquals("Trainee isActive status is already true", result);
    }

    @Test
    public void testActivateTraineeNotFound() {
        //given
        String username = "A.I";
        doThrow(new GymEntityNotFoundException(String.format("Trainee with username %s does not exist.", username)))
                .when(traineeRepository).findByUsername(username);
        assertThrows(GymEntityNotFoundException.class, () ->
                traineeService.changeActiveStatus(username, true),
                String.format("Trainee with username %s does not exist.", username));
    }

    @Test
    public void testGetTraineeProfileSuccess() {
        //given
        String username = "A.A";
        TraineeEntity trainee = new TraineeEntity();
        TrainingEntity training = new TrainingEntity();
        training.setTrainer(new TrainerEntity());
        trainee.setTrainings(List.of(training));
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        //when
        TraineeEntity result = traineeService.getTraineeProfile(username);

        //then
        verify(traineeRepository).findByUsername(username);
        assertEquals(trainee, result);

    }

    @Test
    public void testGetTraineeProfileFailure() {
        //given
        String username = "A.A";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        //then
        assertThrows(GymEntityNotFoundException.class,
                () -> traineeService.getTraineeProfile(username),
                String.format("Trainee with username %s does not exist.", username));

    }

    @Test
    public void testTraineeTrainersList() {
        //given
        String username = "A.A";
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        trainee.setUser(user);
        List<String> trainers = Arrays.asList("A.C", "A.B");
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        TraineeEntity updatedTrainee = new TraineeEntity();
        UserEntity user1 = new UserEntity();
        user1.setUsername(trainers.get(0));
        TrainerEntity trainer1 = new TrainerEntity();
        trainer1.setUser(user1);
        UserEntity user2 = new UserEntity();
        user2.setUsername(trainers.get(1));
        TrainerEntity trainer2 = new TrainerEntity();
        trainer2.setUser(user2);
        updatedTrainee.setTrainers(Set.of(trainer1, trainer2));
        when(trainerService.getTrainerByUsername(trainers.get(0))).thenReturn(trainer1);
        when(trainerService.getTrainerByUsername(trainers.get(1))).thenReturn(trainer2);
        when(traineeRepository.save(trainee)).thenReturn(updatedTrainee);

        //when
        Set<TrainerEntity> result = traineeService.updateTraineesTrainerList(username, trainers);

        //then
        assertEquals(Set.of(trainer1, trainer2), result);

    }

}
