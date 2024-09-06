package facadetest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.facade.TraineeFacade;
import org.example.mapper.TraineeMapper;
import org.example.services.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeFacadeTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TraineeMapper traineeMapper;
    @InjectMocks
    private TraineeFacade traineeFacade;

    @Test
    public void testCreateTraineeSuccess() {
        TraineeDto traineeDto = new TraineeDto();
        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);

        traineeFacade.createTrainee(traineeDto);

        verify(traineeService, times(1)).createTrainee(traineeEntity);
    }

    @Test
    void testCreateTraineeInvalidPassword() {
        TraineeDto traineeDto = new TraineeDto();
        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);

        doThrow(IllegalPasswordException.class).when(traineeService).createTrainee(traineeEntity);
        traineeFacade.createTrainee(traineeDto);

        verify(traineeService, times(1)).createTrainee(traineeEntity);
    }

    @Test
    public void testGetTraineeByUsernameSuccess() {
        String username = "JackSmith";
        TraineeDto traineeDto = new TraineeDto();
        when(traineeService.getTraineeByUsername(username)).thenReturn(traineeDto);

        TraineeDto actualTraineeDto = traineeFacade.getTraineeByUsername(username);
        verify(traineeService, times(1)).getTraineeByUsername(username);
        assertEquals(traineeDto, actualTraineeDto);
    }

    @Test
    public void testGetTraineeByUsernameInvalidUsername() {
        String username = "invalidUsername";
        when(traineeService.getTraineeByUsername(username)).thenThrow(new IllegalUsernameException(username));

        assertThatThrownBy(() -> traineeFacade.getTraineeByUsername(username))
                .isInstanceOf(IllegalUsernameException.class)
                .hasMessageContaining("Illegal username: " + username);

        verify(traineeService, times(1)).getTraineeByUsername(username);
    }

    @Test
    public void testGetTraineeByIdSuccess() {
        Long id = 1L;
        TraineeDto traineeDto = new TraineeDto();

        when(traineeService.getTraineeById(id)).thenReturn(traineeDto);

        TraineeDto traineeDtoActual = traineeService.getTraineeById(id);

        assertEquals(traineeDto, traineeDtoActual);
        verify(traineeService, times(1)).getTraineeById(id);
    }

    @Test
    public void testGetTraineeByIdFailure() {
        Long id = 1L;

        when(traineeService.getTraineeById(id)).thenThrow(new IllegalIdException("No trainee with id: " + id));
        assertThatThrownBy(() -> traineeService.getTraineeById(id))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + id);
        verify(traineeService, times(1)).getTraineeById(id);

    }


}
