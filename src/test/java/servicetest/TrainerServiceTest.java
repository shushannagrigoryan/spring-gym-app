package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.dao.TrainerDao;
import org.example.dao.UserDao;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalPasswordException;
import org.example.mapper.TrainerMapper;
import org.example.services.TrainerService;
import org.example.services.ValidatePassword;
import org.example.storage.SaveDataToFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
    @Mock
    private UserDao userDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private SaveDataToFile saveDataToFile;
    @Mock
    private ValidatePassword validatePassword;

    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    public void testCreateTrainerSuccess() {
        //given
        String password = "myPassword";
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setPassword(password);
        when(validatePassword.passwordNotValid(trainerEntity.getPassword())).thenReturn(false);
        when(userDao.generateUsername(trainerEntity.getFirstName(), trainerEntity.getLastName()))
                .thenReturn("Jack.Jones");
        doNothing().when(trainerDao).createTrainer(trainerEntity);
        doNothing().when(saveDataToFile).writeMapToFile("Trainer");

        //when
        trainerService.createTrainer(trainerEntity);

        //then
        verify(validatePassword).passwordNotValid(trainerEntity.getPassword());
    }

    @Test
    public void testCreateTrainerInvalidPassword() {
        //given
        String password = "myPassword";
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setPassword(password);
        when(validatePassword.passwordNotValid(trainerEntity.getPassword())).thenReturn(true);

        //then
        GymIllegalPasswordException exception =
                assertThrows(GymIllegalPasswordException.class,
                        () -> trainerService.createTrainer(trainerEntity));
        assertEquals("Illegal password: " + password, exception.getMessage());

    }


    @Test
    public void testGetTrainerByUsernameSuccess() {
        //given
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String specialization = "boxing";
        String username = firstName.concat(".").concat(lastName);
        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, password, specialization);
        when(trainerDao.getTrainerByUsername(username)).thenReturn(Optional.of(trainerEntity));

        //when
        TrainerDto trainerDto = trainerService.getTrainerByUsername(username);

        //then
        assertEquals(trainerMapper.entityToDto(trainerEntity), trainerDto);
        verify(trainerDao).getTrainerByUsername(username);
    }


    @Test
    public void testGetTrainerByUsernameFailure() {
        //given
        String username = "John.Smith";

        when(trainerDao.getTrainerByUsername(username)).thenReturn(Optional.empty());

        //when
        TrainerDto result = trainerService.getTrainerByUsername(username);

        //then
        assertNull(result);
        verify(trainerDao).getTrainerByUsername(username);
    }

    @Test
    public void testGetTrainerByIdSuccess() {
        //given
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String specialization = "boxing";
        Long id = 1L;
        TrainerDto trainerDto =
                new TrainerDto(firstName, lastName, password, specialization);

        TrainerEntity trainerEntity =
                new TrainerEntity(firstName, lastName, password, specialization);
        when(trainerMapper.entityToDto(trainerEntity)).thenReturn(trainerDto);
        when(trainerDao.getTrainerById(id)).thenReturn(Optional.of(trainerEntity));

        //when
        TrainerDto result = trainerService.getTrainerById(id);

        //then
        verify(trainerDao).getTrainerById(id);
        assertEquals(trainerDto, result);
    }

    @Test
    public void testGetTrainerByIdFailure() {
        //given
        Long id = 1L;
        when(trainerDao.getTrainerById(id)).thenReturn(Optional.empty());

        //then
        GymIllegalIdException exception =
                assertThrows(GymIllegalIdException.class,
                        () -> trainerService.getTrainerById(id));
        assertEquals("No trainer with id: " + id, exception.getMessage());
        verify(trainerDao).getTrainerById(id);
    }

    @Test
    public void testUpdateTrainerByIdInvalidPassword() {
        //given
        TrainerEntity trainer = new TrainerEntity();
        String password = "myPassword";
        Long id = 1L;
        trainer.setPassword(password);
        when(validatePassword.passwordNotValid(password)).thenReturn(true);

        //then
        GymIllegalPasswordException exception =
                assertThrows(GymIllegalPasswordException.class,
                        () -> trainerService.updateTrainerById(id, trainer));
        assertEquals("Illegal password: " + password, exception.getMessage());
        verify(validatePassword).passwordNotValid(password);

    }

    @Test
    public void testUpdateTrainerByIdInvalidId() {
        //given
        TrainerEntity trainer = new TrainerEntity();
        String password = "myPassword";
        trainer.setPassword(password);
        Long id = 1L;
        when(validatePassword.passwordNotValid(trainer.getPassword())).thenReturn(false);
        doThrow(new GymIllegalIdException("No trainer with id: " + id))
                .when(trainerDao).updateTrainerById(id, trainer);

        //then
        GymIllegalIdException exception =
                assertThrows(GymIllegalIdException.class,
                        () -> trainerService.updateTrainerById(id, trainer));
        assertEquals("No trainer with id: " + id, exception.getMessage());

        verify(trainerDao).updateTrainerById(id, trainer);

    }

    @Test
    public void testUpdateTrainerSuccess() {
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String username = firstName.concat(".").concat(lastName);
        String specialization = "boxing";
        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName,
                password, specialization);
        Long id = 1L;

        when(validatePassword.passwordNotValid(trainerEntity.getPassword())).thenReturn(false);
        when(userDao.generateUsername(trainerEntity.getFirstName(), trainerEntity.getLastName()))
                .thenReturn(username);


        trainerService.updateTrainerById(id, trainerEntity);

        assertEquals(username, trainerEntity.getUsername());
        verify(validatePassword).passwordNotValid(trainerEntity.getPassword());
        verify(userDao).generateUsername(trainerEntity.getFirstName(), trainerEntity.getLastName());
        verify(trainerDao).updateTrainerById(id, trainerEntity);
        verify(saveDataToFile).writeMapToFile("Trainer");
        assertEquals(username, trainerEntity.getUsername());
        assertEquals(id, trainerEntity.getUserId());

    }


}
