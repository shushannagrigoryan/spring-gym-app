package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.dao.TrainerDao;
import org.example.dao.UserDao;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.mapper.TrainerMapper;
import org.example.services.TrainerService;
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
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    public void testCreateTrainerSuccess() {
        //given
        String password = "myPassword";
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setPassword(password);
        when(userDao.generateUsername(trainerEntity.getFirstName(), trainerEntity.getLastName()))
                .thenReturn("Jack.Jones");
        doNothing().when(trainerDao).createTrainer(trainerEntity);
        doNothing().when(saveDataToFile).writeMapToFile("Trainer");

        //when
        trainerService.createTrainer(trainerEntity);

        //then
        verify(userDao).generateUsername(trainerEntity.getFirstName(), trainerEntity.getLastName());
        verify(userDao).generatePassword();
    }

    @Test
    public void testGetTrainerByUsernameSuccess() {
        //given
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String specialization = "boxing";
        String username = firstName.concat(".").concat(lastName);
        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName, specialization);
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
        String specialization = "boxing";
        Long id = 1L;
        TrainerDto trainerDto =
                new TrainerDto(firstName, lastName, specialization);

        TrainerEntity trainerEntity =
                new TrainerEntity(firstName, lastName, specialization);
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
    public void testUpdateTrainerByIdInvalidId() {
        //given
        TrainerEntity trainer = new TrainerEntity();
        String password = "myPassword";
        trainer.setPassword(password);
        Long id = 1L;

        when(trainerDao.getTrainerById(id)).thenReturn(Optional.empty());

        //then
        GymIllegalIdException exception =
                assertThrows(GymIllegalIdException.class,
                        () -> trainerService.updateTrainerById(id, trainer));
        assertEquals("No trainer with id: " + id, exception.getMessage());
    }

    @Test
    public void testUpdateTrainerSuccess() {
        //given
        String firstName = "trainerF1";
        String lastName = "trainerF2";
        String specialization = "boxing";
        TrainerEntity trainerEntity = new TrainerEntity(firstName, lastName,
                specialization);
        Long id = 1L;

        when(trainerDao.getTrainerById(id)).thenReturn(Optional.of(trainerEntity));

        //when
        trainerService.updateTrainerById(id, trainerEntity);

        //then
        verify(trainerDao).updateTrainerById(id, trainerEntity);
        verify(saveDataToFile).writeMapToFile("Trainer");


    }


}
