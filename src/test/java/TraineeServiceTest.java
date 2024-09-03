import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.dao.TraineeDao;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.mapper.TraineeMapper;
import org.example.services.TraineeService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    private Map<Long, TraineeEntity> traineeStorage;
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
    public void testCreateTraineeSuccess(){
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String username = firstName + lastName;
        String address = "myAddress";
        LocalDate dateOfBirth = LocalDate.of(2024,9,3);
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
                password, dateOfBirth, address);

        when(validatePassword.passwordNotValid(traineeEntity.getPassword())).thenReturn(false);
        when(userService.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName())).thenReturn(username);

        traineeService.createTrainee(traineeEntity);

        assertEquals(username, traineeEntity.getUsername());
        verify(traineeDao).createTrainee(traineeEntity);
        verify(saveDataToFile).writeMapToFile("Trainee");
    }

    @Test
    public void createTraineeInvalidPassword(){
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String address = "myAddress";
        LocalDate dateOfBirth = LocalDate.of(2024,9,3);
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
                password, dateOfBirth, address);

        when(validatePassword.passwordNotValid(password)).thenReturn(true);
        assertThatThrownBy(() -> traineeService.createTrainee(traineeEntity))
                .isInstanceOf(IllegalPasswordException.class)
                .hasMessageContaining("Illegal password: " + password);

    }

    @Test
    public void getTraineeByUsernameSuccess(){
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String address = "myAddress";
        String username = firstName + lastName;
        LocalDate dateOfBirth = LocalDate.of(2024,9,3);
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
                password, dateOfBirth, address);

        when(traineeDao.getTraineeByUsername(username)).thenReturn(Optional.of(traineeEntity));

        TraineeDto traineeDto = traineeService.getTraineeByUsername(username);

        assertEquals(traineeMapper.entityToDto(traineeEntity), traineeDto);
        verify(traineeDao, times(1)).getTraineeByUsername(username);
    }

    @Test
    public void getTraineeByUsernameFailure(){
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String username = firstName + lastName;

        when(traineeDao.getTraineeByUsername(username)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> traineeService.getTraineeByUsername(username))
                .isInstanceOf(IllegalUsernameException.class)
                .hasMessageContaining("Illegal username: " + username);
        verify(traineeDao, times(1)).getTraineeByUsername(username);
    }

    @Test
    public void getTraineeByIdSuccess(){
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String address = "myAddress";
        String username = firstName + lastName;
        LocalDate dateOfBirth = LocalDate.of(2024,9,3);
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
    public void getTraineeByIdFailure(){
        Long id = 1L;

        when(traineeDao.getTraineeById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> traineeService.getTraineeById(id))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + id);
        verify(traineeDao, times(1)).getTraineeById(id);
    }

    @Test
    public void deleteTraineeByIdSuccess(){
        long id = 1L;

        doNothing().when(traineeDao).deleteTraineeById(id);
        doNothing().when(saveDataToFile).writeMapToFile("Trainee");

        traineeService.deleteTraineeById(id);

        verify(traineeDao, times(1)).deleteTraineeById(id);
        verify(saveDataToFile, times(1)).writeMapToFile("Trainee");

    }

    @Test
    public void deleteTraineeByIdFailure(){
        long id = 1L;

        doThrow(new IllegalIdException("No trainee with id: " + id)).when(traineeDao).deleteTraineeById(id);
        assertThatThrownBy(() -> traineeService.deleteTraineeById(id))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining(("No trainee with id: " + id));

        verify(traineeDao, times(1)).deleteTraineeById(id);
    }

    @Test
    public void updateTraineeByIdInvalidPassword(){
        TraineeEntity trainee = new TraineeEntity();
        String password = "myPassword";
        Long id = 1L;
        trainee.setPassword(password);
        when(validatePassword.passwordNotValid(password)).thenReturn(true);

        assertThatThrownBy(() -> traineeService.updateTraineeById(id,trainee))
                .isInstanceOf(IllegalPasswordException.class)
                .hasMessageContaining("Illegal password: " + password);
        verify(validatePassword, times(1)).passwordNotValid(password);

    }




}
