package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.example.dao.TraineeDao;
import org.example.dao.UserDao;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
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

    //    @Test
    //    public void testCreateTraineeSuccess() {
    //        //        public void createTrainee(TraineeEntity traineeEntity) {
    //        //            log.debug("Creating trainee: {}", traineeEntity);
    //        //
    //        //            if (validatePassword.passwordNotValid(traineeEntity.getPassword())) {
    //        //                log.debug("Invalid password for trainee");
    //        //                throw new GymIllegalPasswordException(traineeEntity.getPassword());
    //        //            }
    //        //
    //        //            String username = userDao
    //        //            .generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
    //        //            traineeEntity.setUsername(username);
    //        //            traineeDao.createTrainee(traineeEntity);
    //        //            saveDataToFile.writeMapToFile("Trainee");
    //        //            log.debug("Successfully created trainee: {}", traineeEntity);
    //        //        }
    //
    //        //given
    //        String password = "myPassword";
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //        when(traineeEntity.getPassword()).thenReturn(password);
    //        when(validatePassword.passwordNotValid(password)).thenReturn(false);
    //
    //        //when
    //        traineeService.createTrainee(traineeEntity);
    //
    //        //then
    //    }

    //    @Test
    //    public void testCreateTraineeInvalidPassword() {
    //
    //        //given
    //        String password = "myPassword";
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //
    //        when(validatePassword.passwordNotValid(password)).thenReturn(true);
    //        doThrow(new GymIllegalPasswordException(password))
    //                .when(validatePassword).passwordNotValid(password);
    //
    //        //when
    //        traineeService.createTrainee(traineeEntity);
    //
    //        //then
    //        GymIllegalPasswordException exception =
    //                assertThrows(GymIllegalPasswordException.class,
    //                        () -> traineeService.createTrainee(traineeEntity));
    //        assertEquals("Illegal password: " + password, exception.getMessage());
    //        //        assertThatThrownBy(() -> traineeService.createTrainee(traineeEntity))
    //        //                .isInstanceOf(GymIllegalPasswordException.class)
    //        //                .hasMessageContaining("Illegal password: " + password);
    //
    //    }

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

    @Test
    public void testGetTraineeByIdSuccess() {

        //        public TraineeDto getTraineeById(Long id) {
        //            log.debug("Retrieving trainee by id: {}", id);
        //            Optional<TraineeEntity> trainee = traineeDao.getTraineeById(id);
        //            if (trainee.isEmpty()) {
        //                throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
        //            }
        //            log.debug("Successfully retrieved trainee by id: {}", id);
        //            return traineeMapper.entityToDto(trainee.get());
        //        }

        //given
        String firstName = "traineeF1";
        String lastName = "traineeF2";
        String password = "myPassword";
        String address = "myAddress";
        Long id = 1L;
        String username = firstName.concat(".").concat(lastName);
        TraineeDto traineeDto = new TraineeDto();
        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName, password, LocalDate.now(), address);
        //when(traineeMapper.entityToDto(traineeEntity)).thenReturn(traineeDto);
        when(traineeDao.getTraineeById(id)).thenReturn(Optional.of(traineeEntity));

        //when
        TraineeDto result = traineeService.getTraineeById(id);

        //then
        verify(traineeDao).getTraineeById(id);
    }

    //    @Test
    //    public void testGetTraineeByIdFailure() {
    //        Long id = 1L;
    //
    //        when(traineeDao.getTraineeById(id)).thenReturn(Optional.empty());
    //        assertThatThrownBy(() -> traineeService.getTraineeById(id))
    //                .isInstanceOf(GymIllegalIdException.class)
    //                .hasMessageContaining("No trainee with id: " + id);
    //        verify(traineeDao, times(1)).getTraineeById(id);
    //    }

    //    @Test
    //    public void testDeleteTraineeByIdSuccess() {
    //        long id = 1L;
    //
    //        doNothing().when(traineeDao).deleteTraineeById(id);
    //        doNothing().when(saveDataToFile).writeMapToFile("Trainee");
    //
    //        traineeService.deleteTraineeById(id);
    //
    //        verify(traineeDao, times(1)).deleteTraineeById(id);
    //        verify(saveDataToFile, times(1)).writeMapToFile("Trainee");
    //
    //    }

    //    @Test
    //    public void testDeleteTraineeByIdFailure() {
    //        long id = 1L;
    //
    //        doThrow(new GymIllegalIdException("No trainee with id: " + id)).when(traineeDao).deleteTraineeById(id);
    //        assertThatThrownBy(() -> traineeService.deleteTraineeById(id))
    //                .isInstanceOf(GymIllegalIdException.class)
    //                .hasMessageContaining(("No trainee with id: " + id));
    //
    //        verify(traineeDao, times(1)).deleteTraineeById(id);
    //    }

    //    @Test
    //    public void testUpdateTraineeByIdInvalidPassword() {
    //        TraineeEntity trainee = new TraineeEntity();
    //        String password = "myPassword";
    //        Long id = 1L;
    //        trainee.setPassword(password);
    //        when(validatePassword.passwordNotValid(password)).thenReturn(true);
    //
    //        assertThatThrownBy(() -> traineeService.updateTraineeById(id, trainee))
    //                .isInstanceOf(GymIllegalPasswordException.class)
    //                .hasMessageContaining("Illegal password: " + password);
    //        verify(validatePassword, times(1)).passwordNotValid(password);
    //
    //    }
    //
    //    @Test
    //    public void testUpdateTraineeByIdInvalidId() {
    //        TraineeEntity trainee = new TraineeEntity();
    //        Long id = 1L;
    //        doThrow(new GymIllegalIdException("No trainee with id: " + id))
    //        .when(traineeDao).updateTraineeById(id, trainee);
    //
    //        assertThatThrownBy(() -> traineeService.updateTraineeById(id, trainee))
    //                .isInstanceOf(GymIllegalIdException.class)
    //                .hasMessageContaining("No trainee with id: " + id);
    //        verify(traineeDao, times(1)).updateTraineeById(id, trainee);
    //
    //    }

    //    @Test
    //    public void testUpdateTraineeSuccess() {
    //        String firstName = "traineeF1";
    //        String lastName = "traineeF2";
    //        String password = "myPassword";
    //        String username = firstName + lastName;
    //        String address = "myAddress";
    //        LocalDate dateOfBirth = LocalDate.of(2024, 9, 3);
    //        TraineeEntity traineeEntity = new TraineeEntity(firstName, lastName,
    //                password, dateOfBirth, address);
    //        Long id = 1L;
    //
    //        when(validatePassword.passwordNotValid(traineeEntity.getPassword())).thenReturn(false);
    //        when(userDao.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName()))
    //                .thenReturn(username);
    //
    //
    //        traineeService.updateTraineeById(id, traineeEntity);
    //
    //        assertEquals(username, traineeEntity.getUsername());
    //        verify(validatePassword, times(1)).passwordNotValid(traineeEntity.getPassword());
    //        verify(userDao, times(1)).generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
    //        verify(traineeDao, times(1)).updateTraineeById(id, traineeEntity);
    //        verify(saveDataToFile, times(1)).writeMapToFile("Trainee");
    //        assertEquals(username, traineeEntity.getUsername());
    //        assertEquals(id, traineeEntity.getUserId());
    //
    //    }


}
