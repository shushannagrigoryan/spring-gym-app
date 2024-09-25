package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.example.auth.TraineeAuth;
import org.example.dto.TraineeDto;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.exceptions.GymIllegalIdException;
import org.example.facade.TraineeFacade;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainingMapper;
import org.example.services.TraineeService;
import org.example.validation.TraineeValidation;
import org.junit.jupiter.api.BeforeEach;
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
    @Mock
    private TraineeValidation traineeValidation;
    @Mock
    private TraineeAuth traineeAuth;
    @Mock
    private TrainingMapper trainingMapper;
    @InjectMocks
    private TraineeFacade traineeFacade;


    //    @BeforeEach
    //    public void setUp() {
    //        traineeFacade.setDependencies(traineeMapper);
    //    }

    @Test
    public void testCreateTraineeSuccess() {
        //given
        TraineeDto traineeDto = new TraineeDto();
        TraineeEntity traineeEntity = new TraineeEntity();
        doNothing().when(traineeValidation).validateTrainee(traineeDto);
        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);
        doNothing().when(traineeService).createTrainee(traineeEntity);

        //when
        traineeFacade.createTrainee(traineeDto);

        //then
        verify(traineeService).createTrainee(traineeEntity);
    }

    @Test
    public void testGetTraineeByIdSuccess() {
        //given
        Long id = 1L;
        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeService.getTraineeById(id)).thenReturn(traineeEntity);

        //when
        traineeFacade.getTraineeById(id);

        //then
        verify(traineeService).getTraineeById(id);
    }
    @Test
    public void testGetTraineeByUsernameSuccess() {
        //given
        String username = "A.B";
        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeService.getTraineeByUsername(username)).thenReturn(traineeEntity);

        //when
        traineeFacade.getTraineeByUsername(username);

        //then
        verify(traineeService).getTraineeByUsername(username);
    }

    @Test
    public void testChangeTraineePasswordSuccess() {
        //given
        String username = "A.B";
        String password = "1234567890";
        when(traineeAuth.traineeAuth(username, password)).thenReturn(true);

        //when
        traineeFacade.changeTraineePassword(username, password);

        //then
        verify(traineeService).changeTraineePassword(username);
    }

    @Test
    public void testChangeTraineePasswordFailure() {
        //given
        String username = "A.B";
        String password = "1234567890";
        when(traineeAuth.traineeAuth(username, password)).thenReturn(false);

        //when
        traineeFacade.changeTraineePassword(username, password);

        //then
        verify(traineeService, times(0)).changeTraineePassword(username);
    }

    @Test
    public void testActivateTrainee() {
        //given
        Long id = 1L;
        doNothing().when(traineeService).activateTrainee(id);

        //when
        traineeFacade.activateTrainee(id);

        //then
        verify(traineeService).activateTrainee(id);
    }
    @Test
    public void testDeactivateTrainee() {
        //given
        Long id = 1L;
        doNothing().when(traineeService).deactivateTrainee(id);

        //when
        traineeFacade.deactivateTrainee(id);

        //then
        verify(traineeService).deactivateTrainee(id);
    }

    @Test
    public void testDeleteTraineeByUsernameSuccess() {
        //given
        String username = "1234567890";
        doNothing().when(traineeService).deleteTraineeByUsername(username);

        //when
        traineeFacade.deleteTraineeByUsername(username);

        //then
        verify(traineeService).deleteTraineeByUsername(username);
    }

    //    @Test
    //    public void testDeleteTraineeByIdFailure() {
    //        //given
    //        Long id = 1L;
    //        doThrow(new GymIllegalIdException("No trainee with id: " + id))
    //                .when(traineeService).deleteTraineeById(id);
    //
    //        //when
    //        traineeFacade.deleteTraineeById(id);
    //
    //        //then
    //        verify(traineeService).deleteTraineeById(id);
    //        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
    //                () -> traineeService.deleteTraineeById(id));
    //        assertEquals("No trainee with id: " + id, exception.getMessage());
    //    }

    @Test
    public void testUpdateTraineeByIdSuccess() {
        //given
        Long id = 1L;
        TraineeDto traineeDto = new TraineeDto();
        TraineeEntity traineeEntity = new TraineeEntity();
        doNothing().when(traineeValidation).validateTrainee(traineeDto);
        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);
        doNothing().when(traineeService).updateTraineeById(id, traineeEntity);

        //when
        traineeFacade.updateTraineeById(id, traineeDto);

        //then
        verify(traineeService).updateTraineeById(id, traineeEntity);
    }

    @Test
    public void testGetTraineeTrainingsByFilter() {
        // Given
        String traineeUsername = "R.R";
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();
        Long trainingTypeId = 1L;
        String trainerUsername = "O.O";

        List<TrainingEntity> trainingList = Arrays.asList(new TrainingEntity(), new TrainingEntity());
        when(traineeService.getTraineeTrainingsByFilter(traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername))
                .thenReturn(trainingList);

        List<TrainingDto> expectedDtoList = Arrays.asList(new TrainingDto(), new TrainingDto());
        when(trainingMapper.entityToDto(trainingList.get(0))).thenReturn(expectedDtoList.get(0));
        when(trainingMapper.entityToDto(trainingList.get(1))).thenReturn(expectedDtoList.get(1));

        // When
        List<TrainingDto> actualDtoList = traineeFacade.getTraineeTrainingsByFilter(
                traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername);

        // Then
        verify(traineeService).getTraineeTrainingsByFilter(traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername);
        verify(trainingMapper, times(2)).entityToDto(any(TrainingEntity.class));
        assertEquals(expectedDtoList, actualDtoList);
    }

    //    @Test
    //    public void testUpdateTraineeByIdIllegalId() {
    //        //given
    //        Long id = 1L;
    //        TraineeDto traineeDto = new TraineeDto();
    //        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);
    //        when(traineeMapper.dtoToEntity(traineeDto)).thenReturn(traineeEntity);
    //        doThrow(new GymIllegalIdException("No trainee with id: " + id))
    //                .when(traineeService).updateTraineeById(id, traineeEntity);
    //
    //        //when
    //        traineeFacade.updateTraineeById(id, traineeDto);
    //
    //        //then
    //        verify(traineeService).updateTraineeById(id, traineeEntity);
    //        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
    //                () -> traineeService.updateTraineeById(id, traineeEntity));
    //        assertEquals("No trainee with id: " + id, exception.getMessage());
    //    }

}
