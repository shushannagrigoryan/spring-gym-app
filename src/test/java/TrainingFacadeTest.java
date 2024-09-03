import org.example.SaveDataToFile;
import org.example.TrainingType;
import org.example.dao.TrainingDao;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.example.exceptions.IllegalIdException;
import org.example.facade.TrainingFacade;
import org.example.mapper.TrainingMapper;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.services.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingFacadeTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingDao trainingDao;

    @Mock
    private SaveDataToFile saveDataToFile;

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingFacade trainingFacade;

    @Test
    public void testCreateTrainingInvalidTraineeId(){

        long traineeId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTraineeId(traineeId);

        doThrow(new IllegalIdException("No trainee with id: " + traineeId))
                .when(trainingService).createTraining(trainingEntity);

        assertThatThrownBy(() -> trainingService.createTraining(trainingEntity))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + trainingEntity.getTraineeId());

        verify(trainingService, times(1)).createTraining(trainingEntity);
    }

    @Test
    public void testCreateTrainingInvalidTrainerId(){
        long trainerId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainerId(trainerId);

        doThrow(new IllegalIdException("No trainer with id: " + trainerId))
                .when(trainingService).createTraining(trainingEntity);

        assertThatThrownBy(() -> trainingService.createTraining(trainingEntity))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainer with id: " + trainingEntity.getTrainerId());

        verify(trainingService, times(1)).createTraining(trainingEntity);
    }

    @Test
    public void testCreateTrainingSuccess(){
        TrainingDto trainingDto = new TrainingDto();
        TrainingEntity trainingEntity = trainingMapper.dtoToEntity(trainingDto);
        doNothing().when(trainingService).createTraining(trainingEntity);

        trainingFacade.createTraining(trainingDto);

        verify(trainingService, times(1)).createTraining(trainingEntity);
    }

    @Test
    public void testGetTrainingByIdSuccess(){
        Long trainingId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        TrainingDto trainingDto = new TrainingDto();
        trainingEntity.setTrainingId(trainingId);

        when(trainingService.getTrainingById(trainingId)).thenReturn(trainingDto);

        TrainingDto trainingDtoActual = trainingService.getTrainingById(trainingId);

        assertNotNull(trainingDto);
        assertEquals(trainingDto, trainingDtoActual);
        verify(trainingService, times(1)).getTrainingById(trainingId);
    }

    @Test
    public void testGetTrainingByIdInvalidId(){
        Long trainingId = 1L;
        when(trainingService.getTrainingById(trainingId))
                .thenThrow(new IllegalIdException("No training with id: " + trainingId));

        assertThatThrownBy(() -> trainingService.getTrainingById(trainingId))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No training with id: " + trainingId);

        verify(trainingService, times(1)).getTrainingById(trainingId);
    }










}
