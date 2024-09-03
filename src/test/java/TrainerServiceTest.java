import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.dao.TrainerDao;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.mapper.TrainerMapper;
import org.example.services.TrainerService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
    @Mock
    private UserService userService;

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
    public void testCreateTrainerSuccess(){
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String username = firstName + lastName;
        String specialization = "boxing";

        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, password, specialization);

        when(validatePassword.passwordNotValid(trainerEntity.getPassword())).thenReturn(false);
        when(userService.generateUsername(trainerEntity.getFirstName(), trainerEntity.getLastName())).thenReturn(username);

        trainerService.createTrainer(trainerEntity);

        assertEquals(username, trainerEntity.getUsername());
        verify(trainerDao).createTrainer(trainerEntity);
        verify(saveDataToFile).writeMapToFile("Trainer");
    }

    @Test
    public void testCreateTrainerInvalidPassword(){
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String specialization = "boxing";

        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, password, specialization);

        when(validatePassword.passwordNotValid(password)).thenReturn(true);
        assertThatThrownBy(() -> trainerService.createTrainer(trainerEntity))
                .isInstanceOf(IllegalPasswordException.class)
                .hasMessageContaining("Illegal password: " + password);

    }


    @Test
    public void testGetTrainerByUsernameSuccess(){
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String password = "myPassword";
        String specialization = "boxing";
        String username = firstName + lastName;
        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, password, specialization);

        when(trainerDao.getTrainerByUsername(username)).thenReturn(Optional.of(trainerEntity));

        TrainerDto trainerDto = trainerService.getTrainerByUsername(username);

        assertEquals(trainerMapper.entityToDto(trainerEntity), trainerDto);
        verify(trainerDao, times(1)).getTrainerByUsername(username);
    }


    @Test
    public void testGetTrainerByUsernameFailure(){
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String username = firstName + lastName;

        when(trainerDao.getTrainerByUsername(username)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> trainerService.getTrainerByUsername(username))
                .isInstanceOf(IllegalUsernameException.class)
                .hasMessageContaining("Illegal username: " + username);
        verify(trainerDao, times(1)).getTrainerByUsername(username);
    }

    @Test
    public void testGetTrainerByIdSuccess(){
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
    public void testGetTrainerByIdFailure(){
        Long id = 1L;

        when(trainerDao.getTrainerById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> trainerService.getTrainerById(id))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainer with id: " + id);
        verify(trainerDao, times(1)).getTrainerById(id);
    }




}
