package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalStateException;
import org.example.password.PasswordGeneration;
import org.example.repository.TrainerRepository;
import org.example.services.TrainerService;
import org.example.services.TrainingTypeService;
import org.example.username.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
    @Mock
    private UsernameGenerator usernameGenerator;
    @Mock
    private PasswordGeneration passwordGeneration;

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    public void testCreateTraineeSuccess() {
        //given
        String password = "myPassword";
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(new UserEntity());
        trainerEntity.getUser().setPassword(password);
        when(usernameGenerator.generateUsername(trainerEntity.getUser().getFirstName(),
                trainerEntity.getUser().getLastName()))
                .thenReturn("Jack.Jones");
        doNothing().when(trainerRepository).createTrainer(trainerEntity);

        //when
        trainerService.createTrainer(trainerEntity);

        //then
        verify(passwordGeneration).generatePassword();
        verify(usernameGenerator).generateUsername(trainerEntity.getUser().getFirstName(),
                trainerEntity.getUser().getLastName());

    }

    @Test
    public void testGetTraineeByUsernameSuccess() {
        //given
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String username = firstName.concat(".").concat(lastName);
        TrainerEntity trainerEntity = new TrainerEntity();
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        trainerEntity.setUser(user);
        trainerEntity.setSpecialization(new TrainingTypeEntity());
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainerEntity);

        //when
        trainerService.getTrainerByUsername(username);

        //then
        verify(trainerRepository).getTrainerByUsername(username);
    }

    @Test
    public void testGetTrainerByUsernameFailure() {
        //given
        String username = "John.Smith";

        when(trainerRepository.getTrainerByUsername(username)).thenReturn(null);

        //then
        assertThrows(GymEntityNotFoundException.class,
                () -> trainerService.getTrainerByUsername(username),
                String.format("Trainer with username %s does not exist.", username));
        verify(trainerRepository).getTrainerByUsername(username);
    }

    @Test
    public void testGetTrainerByIdSuccess() {
        //given
        TrainerEntity trainerEntity = new TrainerEntity();
        Long id = 1L;
        trainerEntity.setId(id);
        when(trainerRepository.getTrainerById(id)).thenReturn(trainerEntity);

        //when
        trainerService.getTrainerById(id);

        //then
        verify(trainerRepository).getTrainerById(id);
    }

    @Test
    public void testGetTrainerBIdFailure() {
        //given
        Long id = 1L;

        when(trainerRepository.getTrainerById(id)).thenReturn(null);

        //then
        assertThrows(GymIllegalIdException.class,
                () -> trainerService.getTrainerById(id),
                String.format("No trainer with id: %d", id));
        verify(trainerRepository).getTrainerById(id);
    }


    @Test
    public void testChangeTrainerPassword() {
        // Given
        String username = "A.A";
        String generatedPassword = "password12";
        when(passwordGeneration.generatePassword()).thenReturn(generatedPassword);

        //when
        trainerService.changeTrainerPassword(username);

        // then
        verify(trainerRepository).changeTrainerPassword(username, generatedPassword);
    }

    @Test
    public void testActivateTrainerSuccess() {
        // Given
        Long trainerId = 1L;
        UserEntity user = new UserEntity();
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(user);
        when(trainerRepository.getTrainerById(trainerId)).thenReturn(trainerEntity);

        // When
        trainerService.activateTrainer(trainerId);

        // Then
        verify(trainerRepository).activateTrainer(trainerEntity);
    }

    @Test
    public void testActivateTrainerAlreadyActive() {
        // Given
        Long trainerId = 1L;
        UserEntity user = new UserEntity();
        user.setActive(true);
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(user);
        when(trainerRepository.getTrainerById(trainerId)).thenReturn(trainerEntity);

        // Then
        assertThrows(GymIllegalStateException.class,
                () -> trainerService.activateTrainer(trainerId),
                String.format("Trainer with id: %d is already active", trainerId));
    }

    @Test
    public void testActivateTrainerNull() {
        // Given
        Long trainerId = 1L;
        UserEntity user = new UserEntity();
        user.setActive(true);
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(user);
        when(trainerRepository.getTrainerById(trainerId)).thenReturn(null);

        // Then
        assertThrows(GymIllegalIdException.class,
                () -> trainerService.activateTrainer(trainerId),
                String.format("No trainer with %d exists.", trainerId));
    }

    @Test
    public void testDeactivateTrainerSuccess() {
        // Given
        Long trainerId = 1L;
        UserEntity user = new UserEntity();
        user.setActive(true);
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(user);
        when(trainerRepository.getTrainerById(trainerId)).thenReturn(trainerEntity);

        // When
        trainerService.deactivateTrainer(trainerId);

        // Then
        verify(trainerRepository).deactivateTrainer(trainerEntity);
    }

    @Test
    public void testDeactivateTrainerAlreadyActive() {
        // Given
        Long trainerId = 1L;
        UserEntity user = new UserEntity();
        user.setActive(false);
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(user);
        when(trainerRepository.getTrainerById(trainerId)).thenReturn(trainerEntity);

        // Then
        assertThrows(GymIllegalStateException.class,
                () -> trainerService.deactivateTrainer(trainerId),
                String.format("Trainer with id: %d is already inactive", trainerId));
    }

    @Test
    public void testDeactivateTrainerNull() {
        // Given
        Long trainerId = 1L;
        UserEntity user = new UserEntity();
        user.setActive(true);
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(user);
        when(trainerRepository.getTrainerById(trainerId)).thenReturn(null);

        // Then
        assertThrows(GymIllegalIdException.class,
                () -> trainerService.deactivateTrainer(trainerId),
                String.format("No trainer with %d exists.", trainerId));
    }

    @Test
    public void testGetTrainerTrainingsByFilter() {
        // Given
        String trainerUsername = "A.A";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        String traineeUsername = "B.B";

        List<TrainingEntity> expectedTrainings = List.of(new TrainingEntity());
        when(trainerRepository.getTrainerTrainingsByFilter(trainerUsername, fromDate,
                toDate, traineeUsername))
                .thenReturn(expectedTrainings);

        // When
        List<TrainingEntity> actualTrainings = trainerService.getTrainerTrainingsByFilter(
                trainerUsername, fromDate, toDate, traineeUsername);

        // Then
        assertEquals(expectedTrainings, actualTrainings);
        verify(trainerRepository).getTrainerTrainingsByFilter(
                trainerUsername, fromDate, toDate, traineeUsername);
    }


    @Test
    public void testGetTrainersNotAssignedToTrainee() {
        //given
        String username = "A.A";
        when(trainerRepository.getTrainersNotAssignedToTrainee(username)).thenReturn(new ArrayList<>());

        //when
        trainerService.getTrainersNotAssignedToTrainee(username);

        //then
        verify(trainerRepository).getTrainersNotAssignedToTrainee(username);
    }


    @Test
    public void testUpdateTrainerByIdInvalidId() {
        //given
        Long id = 1L;
        trainerRepository.getTrainerById(id);
        when(trainerRepository.getTrainerById(id)).thenReturn(null);

        //then
        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> trainerService.updateTrainerById(id, new TrainerEntity()));
        assertEquals("No trainer with id: " + id, exception.getMessage());
    }

    @Test
    public void testUpdateTraineeSuccess() {
        // given
        String firstName = "A";
        String lastName = "V";
        TrainerEntity trainerEntity = new TrainerEntity();
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        trainerEntity.setUser(user);
        TrainingTypeEntity specialization = new TrainingTypeEntity();
        specialization.setId(1L);
        trainerEntity.setSpecialization(specialization);

        Long id = 1L;

        trainerEntity.setSpecializationId(id);
        UserEntity resultUser = new UserEntity();
        resultUser.setFirstName(firstName);
        resultUser.setLastName(lastName);
        TrainerEntity result = new TrainerEntity();
        result.setUser(resultUser);

        when(trainerRepository.getTrainerById(id)).thenReturn(result);
        when(trainingTypeService.getTrainingTypeById(id)).thenReturn(specialization);
        ArgumentCaptor<TrainerEntity> trainerCaptor = ArgumentCaptor.forClass(TrainerEntity.class);

        // when
        trainerService.updateTrainerById(id, trainerEntity);

        // then
        verify(trainerRepository).updateTrainerById(eq(id), trainerCaptor.capture());
        TrainerEntity updatedTrainer = trainerCaptor.getValue();
        assertEquals(firstName, updatedTrainer.getUser().getFirstName());
        assertEquals(lastName, updatedTrainer.getUser().getLastName());
        assertEquals(id, updatedTrainer.getSpecialization().getId());
    }

}
