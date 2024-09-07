package servicetest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.example.ValidatePassword;
import org.example.dao.TraineeDao;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalPasswordException;
import org.example.mapper.TraineeMapper;
import org.example.services.TraineeService;
import org.example.services.UserService;
import org.example.storage.SaveDataToFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private SaveDataToFile saveDataToFile;
    @Mock
    private ValidatePassword validatePassword;

    @Mock
    private TraineeMapper traineeMapper;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    public void testCreateTraineeSuccess() {
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String username = firstName + lastName;
        String address = "myAddress";
        LocalDate dateOfBirth = LocalDate.of(2024, 9, 3);
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
                password, dateOfBirth, address);

        when(validatePassword.passwordNotValid(traineeEntity.getPassword())).thenReturn(false);
        when(userService.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName()))
                .thenReturn(username);

        traineeService.createTrainee(traineeEntity);

        assertEquals(username, traineeEntity.getUsername());
        verify(traineeDao).createTrainee(traineeEntity);
        verify(saveDataToFile).writeMapToFile("Trainee");
    }

    @Test
    public void testCreateTraineeInvalidPassword() {
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String address = "myAddress";
        LocalDate dateOfBirth = LocalDate.of(2024, 9, 3);
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
                password, dateOfBirth, address);

        when(validatePassword.passwordNotValid(password)).thenReturn(true);
        assertThatThrownBy(() -> traineeService.createTrainee(traineeEntity))
                .isInstanceOf(GymIllegalPasswordException.class)
                .hasMessageContaining("Illegal password: " + password);

    }

    //    @Test
    //    public void testGetTraineeByUsernameSuccess() {
    //        String firstName = "traineeF1";
    //        String lastName = "traineeF2";
    //        String password = "myPassword";
    //        String address = "myAddress";
    //        String username = firstName + lastName;
    //        LocalDate dateOfBirth = LocalDate.of(2024, 9, 3);
    //        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
    //                password, dateOfBirth, address);
    //
    //        when(traineeDao.getTraineeByUsername(username)).thenReturn(Optional.of(traineeEntity));
    //
    //        TraineeDto traineeDto = traineeService.getTraineeByUsername(username);
    //
    //        assertEquals(traineeMapper.entityToDto(traineeEntity), traineeDto);
    //        verify(traineeDao, times(1)).getTraineeByUsername(username);
    //    }

    //    @Test
    //    public void testGetTraineeByUsernameFailure() {
    //        String firstName = "traineeF1";
    //        String lastName = "traineeF2";
    //        String username = firstName + lastName;
    //
    //        when(traineeDao.getTraineeByUsername(username)).thenReturn(Optional.empty());
    //        assertThatThrownBy(() -> traineeService.getTraineeByUsername(username))
    //                .isInstanceOf(IllegalUsernameException.class)
    //                .hasMessageContaining("Illegal username: " + username);
    //        verify(traineeDao, times(1)).getTraineeByUsername(username);
    //    }

    @Test
    public void testGetTraineeByIdSuccess() {
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String address = "myAddress";
        String username = firstName + lastName;
        LocalDate dateOfBirth = LocalDate.of(2024, 9, 3);
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
                password, dateOfBirth, address);
        Long id = 1L;
        traineeEntity.setUserId(id);

        when(traineeDao.getTraineeById(id)).thenReturn(Optional.of(traineeEntity));

        TraineeDto traineeDto = traineeService.getTraineeById(id);

        assertEquals(traineeMapper.entityToDto(traineeEntity), traineeDto);
        verify(traineeDao, times(1)).getTraineeById(id);
    }

    @Test
    public void testGetTraineeByIdFailure() {
        Long id = 1L;

        when(traineeDao.getTraineeById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> traineeService.getTraineeById(id))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + id);
        verify(traineeDao, times(1)).getTraineeById(id);
    }

    @Test
    public void testDeleteTraineeByIdSuccess() {
        long id = 1L;

        doNothing().when(traineeDao).deleteTraineeById(id);
        doNothing().when(saveDataToFile).writeMapToFile("Trainee");

        traineeService.deleteTraineeById(id);

        verify(traineeDao, times(1)).deleteTraineeById(id);
        verify(saveDataToFile, times(1)).writeMapToFile("Trainee");

    }

    @Test
    public void testDeleteTraineeByIdFailure() {
        long id = 1L;

        doThrow(new GymIllegalIdException("No trainee with id: " + id)).when(traineeDao).deleteTraineeById(id);
        assertThatThrownBy(() -> traineeService.deleteTraineeById(id))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining(("No trainee with id: " + id));

        verify(traineeDao, times(1)).deleteTraineeById(id);
    }

    @Test
    public void testUpdateTraineeByIdInvalidPassword() {
        TraineeEntity trainee = new TraineeEntity();
        String password = "myPassword";
        Long id = 1L;
        trainee.setPassword(password);
        when(validatePassword.passwordNotValid(password)).thenReturn(true);

        assertThatThrownBy(() -> traineeService.updateTraineeById(id, trainee))
                .isInstanceOf(GymIllegalPasswordException.class)
                .hasMessageContaining("Illegal password: " + password);
        verify(validatePassword, times(1)).passwordNotValid(password);

    }

    @Test
    public void testUpdateTraineeByIdInvalidId() {
        TraineeEntity trainee = new TraineeEntity();
        Long id = 1L;
        doThrow(new GymIllegalIdException("No trainee with id: " + id)).when(traineeDao).updateTraineeById(id, trainee);

        assertThatThrownBy(() -> traineeService.updateTraineeById(id, trainee))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + id);
        verify(traineeDao, times(1)).updateTraineeById(id, trainee);

    }

    @Test
    public void testUpdateTraineeSuccess() {
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String username = firstName + lastName;
        String address = "myAddress";
        LocalDate dateOfBirth = LocalDate.of(2024, 9, 3);
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
                password, dateOfBirth, address);
        Long id = 1L;

        when(validatePassword.passwordNotValid(traineeEntity.getPassword())).thenReturn(false);
        when(userService.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName()))
                .thenReturn(username);


        traineeService.updateTraineeById(id, traineeEntity);

        assertEquals(username, traineeEntity.getUsername());
        verify(validatePassword, times(1)).passwordNotValid(traineeEntity.getPassword());
        verify(userService, times(1)).generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        verify(traineeDao, times(1)).updateTraineeById(id, traineeEntity);
        verify(saveDataToFile, times(1)).writeMapToFile("Trainee");
        assertEquals(username, traineeEntity.getUsername());
        assertEquals(id, traineeEntity.getUserId());

    }


}
