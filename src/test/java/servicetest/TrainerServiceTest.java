package servicetest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
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
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String username = firstName + lastName;
        String specialization = "boxing";

        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, password, specialization);

        when(validatePassword.passwordNotValid(trainerEntity.getPassword())).thenReturn(false);
        when(userDao.generateUsername(trainerEntity.getFirstName(), trainerEntity.getLastName()))
                .thenReturn(username);

        trainerService.createTrainer(trainerEntity);

        assertEquals(username, trainerEntity.getUsername());
        verify(trainerDao).createTrainer(trainerEntity);
        verify(saveDataToFile).writeMapToFile("Trainer");
    }

    @Test
    public void testCreateTrainerInvalidPassword() {
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String specialization = "boxing";

        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, password, specialization);

        when(validatePassword.passwordNotValid(password)).thenReturn(true);
        assertThatThrownBy(() -> trainerService.createTrainer(trainerEntity))
                .isInstanceOf(GymIllegalPasswordException.class)
                .hasMessageContaining("Illegal password: " + password);

    }


    //    @Test
    //    public void testGetTrainerByUsernameSuccess() {
    //        String firstName = "trainerF1";
    //        String lastName = "trainerF2";
    //        String password = "myPassword";
    //        String specialization = "boxing";
    //        String username = firstName + lastName;
    //        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, password, specialization);
    //
    //        when(trainerDao.getTrainerByUsername(username)).thenReturn(Optional.of(trainerEntity));
    //
    //        TrainerDto trainerDto = trainerService.getTrainerByUsername(username);
    //
    //        assertEquals(trainerMapper.entityToDto(trainerEntity), trainerDto);
    //        verify(trainerDao, times(1)).getTrainerByUsername(username);
    //    }
    //
    //
    //    @Test
    //    public void testGetTrainerByUsernameFailure() {
    //        String firstName = "trainerF1";
    //        String lastName = "trainerF2";
    //        String username = firstName + lastName;
    //
    //        when(trainerDao.getTrainerByUsername(username)).thenReturn(Optional.empty());
    //        assertThatThrownBy(() -> trainerService.getTrainerByUsername(username))
    //                .isInstanceOf(IllegalUsernameException.class)
    //                .hasMessageContaining("Illegal username: " + username);
    //        verify(trainerDao, times(1)).getTrainerByUsername(username);
    //    }

    @Test
    public void testGetTrainerByIdSuccess() {
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String specialization = "boxing";
        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, password, specialization);
        Long id = 1L;
        trainerEntity.setUserId(id);

        when(trainerDao.getTrainerById(id)).thenReturn(Optional.of(trainerEntity));

        TrainerDto trainerDto = trainerService.getTrainerById(id);

        assertEquals(trainerMapper.entityToDto(trainerEntity), trainerDto);
        verify(trainerDao, times(1)).getTrainerById(id);
    }

    @Test
    public void testGetTrainerByIdFailure() {
        Long id = 1L;

        when(trainerDao.getTrainerById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> trainerService.getTrainerById(id))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining("No trainer with id: " + id);
        verify(trainerDao, times(1)).getTrainerById(id);
    }

    @Test
    public void testUpdateTrainerByIdInvalidPassword() {
        TrainerEntity trainer = new TrainerEntity();
        String password = "myPassword";
        Long id = 1L;
        trainer.setPassword(password);
        when(validatePassword.passwordNotValid(password)).thenReturn(true);

        assertThatThrownBy(() -> trainerService.updateTrainerById(id, trainer))
                .isInstanceOf(GymIllegalPasswordException.class)
                .hasMessageContaining("Illegal password: " + password);
        verify(validatePassword, times(1)).passwordNotValid(password);

    }

    @Test
    public void testUpdateTrainerByIdInvalidId() {
        TrainerEntity trainer = new TrainerEntity();
        Long id = 1L;
        doThrow(new GymIllegalIdException("No trainer with id: " + id)).when(trainerDao).updateTrainerById(id, trainer);

        assertThatThrownBy(() -> trainerService.updateTrainerById(id, trainer))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining("No trainer with id: " + id);
        verify(trainerDao, times(1)).updateTrainerById(id, trainer);

    }

    @Test
    public void testUpdateTrainerSuccess() {
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String specialization = "boxing";
        String username = firstName + lastName;
        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, password, specialization);
        Long id = 1L;

        when(validatePassword.passwordNotValid(trainerEntity.getPassword())).thenReturn(false);
        when(userDao.generateUsername(trainerEntity.getFirstName(), trainerEntity.getLastName()))
                .thenReturn(username);


        trainerService.updateTrainerById(id, trainerEntity);

        assertEquals(username, trainerEntity.getUsername());
        verify(validatePassword, times(1)).passwordNotValid(trainerEntity.getPassword());
        verify(userDao, times(1)).generateUsername(trainerEntity.getFirstName(),
                trainerEntity.getLastName());
        verify(trainerDao, times(1)).updateTrainerById(id, trainerEntity);
        verify(saveDataToFile, times(1)).writeMapToFile("Trainer");
        assertEquals(username, trainerEntity.getUsername());
        assertEquals(id, trainerEntity.getUserId());

    }


}
