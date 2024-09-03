import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.dao.TraineeDao;
import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
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


}
