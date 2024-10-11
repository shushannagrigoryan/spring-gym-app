package servicetest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.password.PasswordGeneration;
import org.example.repository.TraineeRepository;
import org.example.services.TraineeService;
import org.example.services.UserService;
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
    private UserService userService;

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
        when(userService.registerUser(user)).thenReturn(user);
        when(traineeRepository.save(traineeEntity)).thenReturn(new TraineeEntity());

        //when
        traineeService.registerTrainee(traineeEntity);

        //then
        verify(passwordGeneration).generatePassword();
        verify(usernameGenerator).generateUsername(traineeEntity.getUser().getFirstName(),
                traineeEntity.getUser().getLastName());

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
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(traineeEntity));

        //when
        traineeService.getTraineeByUsername(username);

        //then
        verify(traineeRepository).findByUser_Username(username);
    }

    @Test
    public void testGetTraineeByUsernameFailure() {
        //given
        String username = "John.Smith";

        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.empty());

        //then
        assertThrows(GymEntityNotFoundException.class,
                () -> traineeService.getTraineeByUsername(username),
                String.format("Trainee with username %s does not exist.", username));
        verify(traineeRepository).findByUser_Username(username);
    }

    @Test
    public void testGetTraineeByIdSuccess() {
        //given
        TraineeEntity traineeEntity = new TraineeEntity();
        Long id = 1L;
        traineeEntity.setId(id);
        when(traineeRepository.findById(id)).thenReturn(Optional.of(traineeEntity));

        //when
        traineeService.getTraineeById(id);

        //then
        verify(traineeRepository).findById(id);
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
        trainee.setId(1L);
        trainee.setUser(user);
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));

        doNothing().when(traineeRepository).deleteById(trainee.getId());
        doNothing().when(userService).deleteUser(user);

        //when
        traineeService.deleteTraineeByUsername(username);

        //then
        verify(traineeRepository).deleteById(trainee.getId());
    }


    //    @Test
    //    public void testUpdateTraineeByIdInvalidId() {
    //        //given
    //        Long id = 1L;
    //        traineeRepository.findById(id);
    //        when(traineeRepository.findById(id)).thenReturn(null);
    //
    //        //then
    //        RuntimeException exception =
    //                assertThrows(RuntimeException.class,
    //                        () -> traineeService.updateTraineeById(id, new TraineeEntity()));
    //        assertEquals("No trainee with id: " + id, exception.getMessage());
    //    }

    //    @Test
    //    public void testUpdateTraineeSuccess() {
    //        // given
    //        String firstName = "A";
    //        String lastName = "V";
    //        String address = "myAddress";
    //        LocalDate dateOfBirth = LocalDate.of(2024, 9, 3);
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //        UserEntity user = new UserEntity();
    //        user.setFirstName(firstName);
    //        user.setLastName(lastName);
    //        traineeEntity.setUser(user);
    //        traineeEntity.setDateOfBirth(dateOfBirth);
    //        traineeEntity.setAddress(address);
    //        Long id = 1L;
    //
    //        UserEntity resultUser = new UserEntity();
    //        resultUser.setFirstName("B");
    //        resultUser.setLastName(lastName);
    //        TraineeEntity result = new TraineeEntity();
    //        result.setUser(resultUser);
    //
    //        when(traineeRepository.getTraineeById(id)).thenReturn(result);
    //        ArgumentCaptor<TraineeEntity> traineeCaptor = ArgumentCaptor.forClass(TraineeEntity.class);
    //
    //        // when
    //        traineeService.updateTraineeById(id, traineeEntity);
    //
    //        // then
    //        verify(traineeRepository).updateTraineeById(eq(id), traineeCaptor.capture());
    //        TraineeEntity updatedTrainee = traineeCaptor.getValue();
    //        assertEquals(firstName, updatedTrainee.getUser().getFirstName());
    //        assertEquals(lastName, updatedTrainee.getUser().getLastName());
    //        assertEquals(dateOfBirth, updatedTrainee.getDateOfBirth());
    //        assertEquals(address, updatedTrainee.getAddress());
    //    }


    //    @Test
    //    public void testChangeTraineePassword() {
    //        // Given
    //        String username = "A.A";
    //        String generatedPassword = "password12";
    //        when(passwordGeneration.generatePassword()).thenReturn(generatedPassword);
    //
    //        //when
    //        traineeService.changeTraineePassword(username);
    //
    //        // then
    //        verify(traineeRepository).changeTraineePassword(username, generatedPassword);
    //    }

    //    @Test
    //    public void testActivateTraineeSuccess() {
    //        // Given
    //        Long traineeId = 1L;
    //        UserEntity user = new UserEntity();
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //        traineeEntity.setUser(user);
    //        when(traineeRepository.getTraineeById(traineeId)).thenReturn(traineeEntity);
    //
    //        // When
    //        traineeService.activateTrainee(traineeId);
    //
    //        // Then
    //        verify(traineeRepository).activateTrainee(traineeEntity);
    //    }
    //
    //    @Test
    //    public void testActivateTraineeAlreadyActive() {
    //        // Given
    //        Long traineeId = 1L;
    //        UserEntity user = new UserEntity();
    //        user.setActive(true);
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //        traineeEntity.setUser(user);
    //        when(traineeRepository.getTraineeById(traineeId)).thenReturn(traineeEntity);
    //
    //        // Then
    //        assertThrows(GymIllegalStateException.class,
    //                () -> traineeService.activateTrainee(traineeId),
    //                String.format("Trainee with id: %d is already active", traineeId));
    //    }
    //
    //    @Test
    //    public void testActivateTraineeNull() {
    //        // Given
    //        Long traineeId = 1L;
    //        UserEntity user = new UserEntity();
    //        user.setActive(true);
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //        traineeEntity.setUser(user);
    //        when(traineeRepository.getTraineeById(traineeId)).thenReturn(null);
    //
    //        // Then
    //        assertThrows(GymIllegalIdException.class,
    //                () -> traineeService.activateTrainee(traineeId),
    //                String.format("No trainee with %d exists.", traineeId));
    //    }
    //
    //    @Test
    //    public void testDeactivateTraineeSuccess() {
    //        // Given
    //        Long traineeId = 1L;
    //        UserEntity user = new UserEntity();
    //        user.setActive(true);
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //        traineeEntity.setUser(user);
    //        when(traineeRepository.getTraineeById(traineeId)).thenReturn(traineeEntity);
    //
    //        // When
    //        traineeService.deactivateTrainee(traineeId);
    //
    //        // Then
    //        verify(traineeRepository).deactivateTrainee(traineeEntity);
    //    }
    //
    //    @Test
    //    public void testDeactivateTraineeAlreadyActive() {
    //        // Given
    //        Long traineeId = 1L;
    //        UserEntity user = new UserEntity();
    //        user.setActive(false);
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //        traineeEntity.setUser(user);
    //        when(traineeRepository.getTraineeById(traineeId)).thenReturn(traineeEntity);
    //
    //        // Then
    //        assertThrows(GymIllegalStateException.class,
    //                () -> traineeService.deactivateTrainee(traineeId),
    //                String.format("Trainee with id: %d is already inactive", traineeId));
    //    }
    //
    //    @Test
    //    public void testDeactivateTraineeNull() {
    //        // Given
    //        Long traineeId = 1L;
    //        UserEntity user = new UserEntity();
    //        user.setActive(true);
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //        traineeEntity.setUser(user);
    //        when(traineeRepository.getTraineeById(traineeId)).thenReturn(null);
    //
    //        // Then
    //        assertThrows(GymIllegalIdException.class,
    //                () -> traineeService.deactivateTrainee(traineeId),
    //                String.format("No trainee with %d exists.", traineeId));
    //    }


}
