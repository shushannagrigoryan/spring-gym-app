package facadeTest;

import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.facade.TrainerFacade;
import org.example.mapper.TrainerMapper;
import org.example.services.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerFacadeTest {
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainerMapper trainerMapper;
    @InjectMocks
    private TrainerFacade trainerFacade;

    @Test
    public void testCreateTrainerSuccess(){
        TrainerDto trainerDto = new TrainerDto();
        TrainerEntity trainerEntity = new TrainerEntity();
        when(trainerMapper.dtoToEntity(trainerDto)).thenReturn(trainerEntity);

        trainerFacade.createTrainer(trainerDto);

        verify(trainerService, times(1)).createTrainer(trainerEntity);
    }

    @Test
    void testCreateTrainerInvalidPassword() {
        TrainerDto trainerDto = new TrainerDto();
        TrainerEntity trainerEntity = new TrainerEntity();
        when(trainerMapper.dtoToEntity(trainerDto)).thenReturn(trainerEntity);

        doThrow(IllegalPasswordException.class).when(trainerService).createTrainer(trainerEntity);
        trainerFacade.createTrainer(trainerDto);

        verify(trainerService, times(1)).createTrainer(trainerEntity);
    }

    @Test
    public void testGetTrainerByUsernameSuccess(){
        String username = "JackSmith";
        TrainerDto trainerDto = new TrainerDto();
        when(trainerService.getTrainerByUsername(username)).thenReturn(trainerDto);

        TrainerDto actualTrainerDto = trainerFacade.getTrainerByUsername(username);
        verify(trainerService, times(1)).getTrainerByUsername(username);
        assertEquals(trainerDto, actualTrainerDto);
    }

    @Test
    public void testGetTrainerByUsernameInvalidUsername() {
        String username = "invalidUsername";
        when(trainerService.getTrainerByUsername(username)).thenThrow(new IllegalUsernameException(username));

        assertThatThrownBy(() -> trainerFacade.getTrainerByUsername(username))
                .isInstanceOf(IllegalUsernameException.class)
                .hasMessageContaining("Illegal username: " + username);

        verify(trainerService, times(1)).getTrainerByUsername(username);
    }

    @Test
    public void testGetTrainerByIdSuccess(){
        Long id = 1L;
        TrainerDto trainerDto = new TrainerDto();

        when(trainerService.getTrainerById(id)).thenReturn(trainerDto);

        TrainerDto trainerDtoActual = trainerService.getTrainerById(id);

        assertEquals(trainerDto, trainerDtoActual);
        verify(trainerService, times(1)).getTrainerById(id);
    }

    @Test
    public void testGetTrainerByIdFailure(){
        Long id = 1L;

        when(trainerService.getTrainerById(id)).thenThrow(new IllegalIdException("No trainer with id: " + id));
        assertThatThrownBy(() -> trainerService.getTrainerById(id))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainer with id: " + id);
        verify(trainerService, times(1)).getTrainerById(id);

    }


}
