package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.example.dao.TraineeDao;
import org.example.dao.UserDao;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalPasswordException;
import org.example.mapper.TraineeMapper;
import org.example.services.TraineeService;
import org.example.services.ValidatePassword;
import org.example.storage.SaveDataToFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    private UserDao userDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private SaveDataToFile saveDataToFile;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private ValidatePassword validatePassword;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    public void testCreateTraineeSuccess() {
        //given
        String password = "myPassword";
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setPassword(password);
        when(validatePassword.passwordNotValid(traineeEntity.getPassword())).thenReturn(false);
        when(userDao.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName()))
                .thenReturn("Jack.Jones");
        doNothing().when(traineeDao).createTrainee(traineeEntity);
        doNothing().when(saveDataToFile).writeMapToFile("Trainee");

        //when
        traineeService.createTrainee(traineeEntity);

        //then
        verify(validatePassword).passwordNotValid(traineeEntity.getPassword());
    }

    @Test
    public void testCreateTraineeInvalidPassword() {
        //given
        String password = "myPassword";
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setPassword(password);
        when(validatePassword.passwordNotValid(traineeEntity.getPassword())).thenReturn(true);

        //then
        GymIllegalPasswordException exception =
                assertThrows(GymIllegalPasswordException.class,
                        () -> traineeService.createTrainee(traineeEntity));
        assertEquals("Illegal password: " + password, exception.getMessage());
    }

    @Test
    public void testGetTraineeByUsernameSuccess() {
        //given
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String address = "myAddress";
        String username = firstName.concat(".").concat(lastName);
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName, password, LocalDate.now(), address);
        when(traineeDao.getTraineeByUsername(username)).thenReturn(Optional.of(traineeEntity));

        //when
        TraineeDto traineeDto = traineeService.getTraineeByUsername(username);

        //then
        assertEquals(traineeMapper.entityToDto(traineeEntity), traineeDto);
        verify(traineeDao).getTraineeByUsername(username);
    }

    @Test
    public void testGetTraineeByUsernameFailure() {
        //given
        String username = "John.Smith";

        when(traineeDao.getTraineeByUsername(username)).thenReturn(Optional.empty());

        //when
        TraineeDto result = traineeService.getTraineeByUsername(username);

        //then
        assertNull(result);
        verify(traineeDao).getTraineeByUsername(username);
    }

    //        @Test
    //        public void testGetTraineeByIdSuccess() {
    //            //given
    //            String firstName = "traineeF1";
    //            String lastName = "traineeF2";
    //            String password = "myPassword";
    //            String address = "myAddress";
    //            Long id = 1L;
    //            String username = firstName.concat(".").concat(lastName);
    //            TraineeDto traineeDto = new TraineeDto(firstName, lastName, password,LocalDate.now(),address);
    //
    //            TraineeEntity traineeEntity =
    //            new TraineeEntity(firstName, lastName, password, LocalDate.now(), address);
    //            when(traineeMapper.entityToDto(traineeEntity)).thenReturn(traineeDto);
    //            when(traineeDao.getTraineeById(id)).thenReturn(Optional.of(traineeEntity));
    //
    //            //when
    //            TraineeDto result = traineeService.getTraineeById(id);
    //
    //            //then
    //            verify(traineeDao).getTraineeById(id);
    //        }

    @Test
    public void testGetTraineeByIdFailure() {
        //given
        Long id = 1L;
        when(traineeDao.getTraineeById(id)).thenReturn(Optional.empty());

        //then
        GymIllegalIdException exception =
                assertThrows(GymIllegalIdException.class,
                        () -> traineeService.getTraineeById(id));
        assertEquals("No trainee with id: " + id, exception.getMessage());
        verify(traineeDao).getTraineeById(id);
    }

    @Test
    public void testDeleteTraineeByIdSuccess() {
        //given
        long id = 1L;

        doNothing().when(traineeDao).deleteTraineeById(id);
        doNothing().when(saveDataToFile).writeMapToFile("Trainee");

        //when
        traineeService.deleteTraineeById(id);

        //then
        verify(traineeDao).deleteTraineeById(id);
        verify(saveDataToFile).writeMapToFile("Trainee");

    }

    @Test
    public void testDeleteTraineeByIdFailure() {
        //given
        long id = 1L;
        doThrow(new GymIllegalIdException("No trainee with id: " + id)).when(traineeDao).deleteTraineeById(id);

        //then
        GymIllegalIdException exception =
                assertThrows(GymIllegalIdException.class,
                        () -> traineeService.deleteTraineeById(id));
        assertEquals("No trainee with id: " + id, exception.getMessage());
        verify(traineeDao).deleteTraineeById(id);
    }

    @Test
    public void testUpdateTraineeByIdInvalidPassword() {
        //given
        TraineeEntity trainee = new TraineeEntity();
        String password = "myPassword";
        Long id = 1L;
        trainee.setPassword(password);
        when(validatePassword.passwordNotValid(password)).thenReturn(true);

        //then
        GymIllegalPasswordException exception =
                assertThrows(GymIllegalPasswordException.class,
                        () -> traineeService.updateTraineeById(id, trainee));
        assertEquals("Illegal password: " + password, exception.getMessage());
        verify(validatePassword).passwordNotValid(password);

    }

    @Test
    public void testUpdateTraineeByIdInvalidId() {
        //given
        TraineeEntity trainee = new TraineeEntity();
        String password = "myPassword";
        trainee.setPassword(password);
        Long id = 1L;
        when(validatePassword.passwordNotValid(trainee.getPassword())).thenReturn(false);
        doThrow(new GymIllegalIdException("No trainee with id: " + id))
        .when(traineeDao).updateTraineeById(id, trainee);

        //then
        GymIllegalIdException exception =
                assertThrows(GymIllegalIdException.class,
                        () -> traineeService.updateTraineeById(id, trainee));
        assertEquals("No trainee with id: " + id, exception.getMessage());

        verify(traineeDao).updateTraineeById(id, trainee);
    }

    @Test
    public void testUpdateTraineeSuccess() {
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String username = firstName.concat(".").concat(lastName);
        String address = "myAddress";
        LocalDate dateOfBirth = LocalDate.of(2024, 9, 3);
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
                password, dateOfBirth, address);
        Long id = 1L;

        when(validatePassword.passwordNotValid(traineeEntity.getPassword())).thenReturn(false);
        when(userDao.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName()))
                .thenReturn(username);


        traineeService.updateTraineeById(id, traineeEntity);

        assertEquals(username, traineeEntity.getUsername());
        verify(validatePassword).passwordNotValid(traineeEntity.getPassword());
        verify(userDao).generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        verify(traineeDao).updateTraineeById(id, traineeEntity);
        verify(saveDataToFile).writeMapToFile("Trainee");
        assertEquals(username, traineeEntity.getUsername());
        assertEquals(id, traineeEntity.getUserId());

    }


}
