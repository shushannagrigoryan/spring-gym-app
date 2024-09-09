package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.facade.TrainingFacade;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TrainingFacadeTest {
    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingFacade trainingFacade;

    @BeforeEach
    public void setUp() {
        trainingFacade.setDependencies(trainingMapper);
    }

    @Test
    public void testCreateTrainingInvalidTraineeId() {
        //given
        long traineeId = 1L;
        TrainingDto trainingDto = new TrainingDto();
        TrainingEntity trainingEntity = new TrainingEntity();
        when(trainingMapper.dtoToEntity(trainingDto)).thenReturn(trainingEntity);
        doThrow(new GymIllegalIdException("No trainee with id: " + traineeId))
                .when(trainingService).createTraining(trainingEntity);

        //when
        trainingFacade.createTraining(trainingDto);

        //then
        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
                () -> trainingService.createTraining(trainingEntity));

        assertEquals("No trainee with id: " + traineeId, exception.getMessage());
    }

    @Test
    public void testCreateTrainingInvalidTrainerId() {
        //given
        long trainerId = 1L;
        TrainingDto trainingDto = new TrainingDto();
        TrainingEntity trainingEntity = new TrainingEntity();
        when(trainingMapper.dtoToEntity(trainingDto)).thenReturn(trainingEntity);
        doThrow(new GymIllegalIdException("No trainer with id: " + trainerId))
                .when(trainingService).createTraining(trainingEntity);

        //when
        trainingFacade.createTraining(trainingDto);

        //then
        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
                () -> trainingService.createTraining(trainingEntity));

        assertEquals("No trainer with id: " + trainerId, exception.getMessage());
    }

    @Test
    public void testCreateTrainingSuccess() {
        //given
        TrainingDto trainingDto = new TrainingDto();
        TrainingEntity trainingEntity = trainingMapper.dtoToEntity(trainingDto);
        doNothing().when(trainingService).createTraining(trainingEntity);


        //when
        trainingFacade.createTraining(trainingDto);

        //then
        verify(trainingService).createTraining(trainingEntity);
    }

    @Test
    public void testGetTrainingByIdSuccess() {
        //given
        Long trainingId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        TrainingDto trainingDto = new TrainingDto();
        trainingEntity.setTrainingId(trainingId);
        when(trainingService.getTrainingById(trainingId)).thenReturn(trainingDto);

        //when
        trainingFacade.getTrainingById(trainingId);

        //then
        verify(trainingService).getTrainingById(trainingId);
    }

    @Test
    public void testGetTrainingByIdInvalidId() {
        //given
        Long trainingId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingId(trainingId);
        doThrow(new GymIllegalIdException("No training with id: " + trainingId))
                .when(trainingService).getTrainingById(trainingId);

        //when
        trainingFacade.getTrainingById(trainingId);

        //then
        GymIllegalIdException exception =
                assertThrows(GymIllegalIdException.class,
                        () -> trainingService.getTrainingById(trainingId));
        assertEquals("No training with id: " + trainingId, exception.getMessage());
    }


}
