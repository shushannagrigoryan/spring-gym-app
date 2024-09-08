package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalPasswordException;
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
        //given
        TraineeDto traineeDto = new TraineeDto();
        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);
        doNothing().when(traineeService).createTrainee(traineeEntity);

        //when
        traineeFacade.createTrainee(traineeDto);

        //then
        verify(traineeService).createTrainee(traineeEntity);
    }

    @Test
    void testCreateTraineeInvalidPassword() {
        //given
        TraineeDto traineeDto = new TraineeDto();
        String password = "illegalPassword";
        traineeDto.setPassword(password);
        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);
        doThrow(new GymIllegalPasswordException(password))
                .when(traineeService).createTrainee(traineeEntity);

        //when
        traineeFacade.createTrainee(traineeDto);

        //then
        GymIllegalPasswordException exception = assertThrows(GymIllegalPasswordException.class,
                () -> traineeService.createTrainee(traineeEntity));
        assertEquals("Illegal password: " + password, exception.getMessage());
    }

    @Test
    public void testGetTraineeByIdSuccess() {
        //given
        Long id = 1L;
        TraineeDto traineeDto = new TraineeDto();
        when(traineeService.getTraineeById(id)).thenReturn(traineeDto);

        //when
        traineeFacade.getTraineeById(id);

        //then
        verify(traineeService).getTraineeById(id);
    }

    @Test
    public void testGetTraineeByIdFailure() {
        //given
        Long id = 1L;
        doThrow(new GymIllegalIdException("No trainee with id: " + id))
                .when(traineeService).getTraineeById(id);

        //when
        traineeFacade.getTraineeById(id);

        //then
        verify(traineeService).getTraineeById(id);
        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
                () -> traineeService.getTraineeById(id));
        assertEquals("No trainee with id: " + id, exception.getMessage());
    }

    @Test
    public void testDeleteTraineeByIdSuccess() {
        //given
        Long id = 1L;
        doNothing().when(traineeService).deleteTraineeById(id);

        //when
        traineeFacade.deleteTraineeById(id);

        //then
        verify(traineeService).deleteTraineeById(id);
    }

    @Test
    public void testDeleteTraineeByIdFailure() {
        //given
        Long id = 1L;
        doThrow(new GymIllegalIdException("No trainee with id: " + id))
                .when(traineeService).deleteTraineeById(id);

        //when
        traineeFacade.deleteTraineeById(id);

        //then
        verify(traineeService).deleteTraineeById(id);
        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
                () -> traineeService.deleteTraineeById(id));
        assertEquals("No trainee with id: " + id, exception.getMessage());
    }

    @Test
    public void testUpdateTraineeByIdSuccess() {
        //given
        Long id = 1L;
        TraineeDto traineeDto = new TraineeDto();
        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);
        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);
        doNothing().when(traineeService).updateTraineeById(id, traineeEntity);

        //when
        traineeFacade.updateTraineeById(id, traineeDto);

        //then
        verify(traineeService).updateTraineeById(id, traineeEntity);
    }

    @Test
    public void testUpdateTraineeByIdInvalidPassword() {
        //given
        Long id = 1L;
        TraineeDto traineeDto = new TraineeDto();
        String password = "illegalPassword";
        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);
        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);
        doThrow(new GymIllegalPasswordException(password))
                .when(traineeService).updateTraineeById(id, traineeEntity);

        //when
        traineeFacade.updateTraineeById(id, traineeDto);

        //then
        verify(traineeService).updateTraineeById(id, traineeEntity);
        GymIllegalPasswordException exception = assertThrows(GymIllegalPasswordException.class,
                () -> traineeService.updateTraineeById(id, traineeEntity));
        assertEquals("Illegal password: " + password, exception.getMessage());

    }

    @Test
    public void testUpdateTraineeByIdIllegalId() {
        //given
        Long id = 1L;
        TraineeDto traineeDto = new TraineeDto();
        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);
        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);
        doThrow(new GymIllegalIdException("No trainee with id: " + id))
                .when(traineeService).updateTraineeById(id, traineeEntity);

        //when
        traineeFacade.updateTraineeById(id, traineeDto);

        //then
        verify(traineeService).updateTraineeById(id, traineeEntity);
        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
                () -> traineeService.updateTraineeById(id, traineeEntity));
        assertEquals("No trainee with id: " + id, exception.getMessage());
    }

}
