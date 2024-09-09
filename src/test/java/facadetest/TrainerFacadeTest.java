package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalPasswordException;
import org.example.facade.TrainerFacade;
import org.example.mapper.TrainerMapper;
import org.example.services.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerFacadeTest {
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainerMapper trainerMapper;
    @InjectMocks
    private TrainerFacade trainerFacade;

    @BeforeEach
    public void setUp() {
        trainerFacade.setDependencies(trainerMapper);
    }

    @Test
    public void testCreateTrainerSuccess() {
        //given
        TrainerDto trainerDto = new TrainerDto();
        TrainerEntity trainerEntity = new TrainerEntity();
        when(trainerMapper.dtoToEntity(trainerDto)).thenReturn(trainerEntity);
        doNothing().when(trainerService).createTrainer(trainerEntity);

        //when
        trainerFacade.createTrainer(trainerDto);

        //then
        verify(trainerService).createTrainer(trainerEntity);
    }

    @Test
    void testCreateTrainerInvalidPassword() {
        //given
        TrainerDto trainerDto = new TrainerDto();
        String password = "illegalPassword";
        trainerDto.setPassword(password);
        TrainerEntity trainerEntity = new TrainerEntity();
        when(trainerMapper.dtoToEntity(trainerDto)).thenReturn(trainerEntity);
        doThrow(new GymIllegalPasswordException(password))
                .when(trainerService).createTrainer(trainerEntity);

        //when
        trainerFacade.createTrainer(trainerDto);

        //then
        GymIllegalPasswordException exception = assertThrows(GymIllegalPasswordException.class,
                () -> trainerService.createTrainer(trainerEntity));
        assertEquals("Illegal password: " + password, exception.getMessage());
    }

    @Test
    public void testGetTrainerByIdSuccess() {
        //given
        Long id = 1L;
        TrainerDto trainerDto = new TrainerDto();
        when(trainerService.getTrainerById(id)).thenReturn(trainerDto);

        //when
        trainerFacade.getTrainerById(id);

        //then
        verify(trainerService).getTrainerById(id);
    }

    @Test
    public void testGetTrainerByIdFailure() {
        //given
        Long id = 1L;
        doThrow(new GymIllegalIdException("No trainer with id: " + id))
                .when(trainerService).getTrainerById(id);

        //when
        trainerFacade.getTrainerById(id);

        //then
        verify(trainerService).getTrainerById(id);
        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
                () -> trainerService.getTrainerById(id));
        assertEquals("No trainer with id: " + id, exception.getMessage());
    }




    @Test
    public void testUpdateTrainerByIdSuccess() {
        //given
        Long id = 1L;
        TrainerDto trainerDto = new TrainerDto();
        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
        when(trainerMapper.dtoToEntity(trainerDto)).thenReturn(trainerEntity);
        doNothing().when(trainerService).updateTrainerById(id, trainerEntity);

        //when
        trainerFacade.updateTrainerById(id, trainerDto);

        //then
        verify(trainerService).updateTrainerById(id, trainerEntity);
    }

    @Test
    public void testUpdateTrainerByIdInvalidPassword() {
        //given
        Long id = 1L;
        TrainerDto trainerDto = new TrainerDto();
        String password = "illegalPassword";
        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
        when(trainerMapper.dtoToEntity(trainerDto)).thenReturn(trainerEntity);
        doThrow(new GymIllegalPasswordException(password))
                .when(trainerService).updateTrainerById(id, trainerEntity);

        //when
        trainerFacade.updateTrainerById(id, trainerDto);

        //then
        verify(trainerService).updateTrainerById(id, trainerEntity);
        GymIllegalPasswordException exception = assertThrows(GymIllegalPasswordException.class,
                () -> trainerService.updateTrainerById(id, trainerEntity));
        assertEquals("Illegal password: " + password, exception.getMessage());

    }

    @Test
    public void testUpdateTrainerByIdIllegalId() {
        //given
        Long id = 1L;
        TrainerDto trainerDto = new TrainerDto();
        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
        when(trainerMapper.dtoToEntity(trainerDto)).thenReturn(trainerEntity);
        doThrow(new GymIllegalIdException("No trainer with id: " + id))
                .when(trainerService).updateTrainerById(id, trainerEntity);

        //when
        trainerFacade.updateTrainerById(id, trainerDto);

        //then
        verify(trainerService).updateTrainerById(id, trainerEntity);
        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
                () -> trainerService.updateTrainerById(id, trainerEntity));
        assertEquals("No trainer with id: " + id, exception.getMessage());
    }

}
