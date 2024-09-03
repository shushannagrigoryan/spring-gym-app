import org.example.SaveDataToFile;
import org.example.TrainingType;
import org.example.dao.TrainingDao;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.example.exceptions.IllegalIdException;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingDao trainingDao;

    @Mock
    private SaveDataToFile saveDataToFile;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    public void testCreateTrainingInvalidTraineeId(){

        Long traineeId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity(traineeId, 2L, "Boxing",
                TrainingType.CARDIO, LocalDateTime.now(), Duration.ofHours(1));

        when(traineeService.getTraineeById(traineeId)).thenReturn(null);

        assertThatThrownBy(() -> trainingService.createTraining(trainingEntity))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + trainingEntity.getTraineeId());

        verify(traineeService, times(1)).getTraineeById(traineeId);
    }

    @Test
    public void testCreateTrainingInvalidTrainerId(){

        Long trainerId = 1L;
        TraineeEntity traineeEntity = new TraineeEntity();
        Long traineeId = 1L;
        traineeEntity.setUserId(traineeId);
        TrainingEntity trainingEntity = new TrainingEntity(traineeId, trainerId, "Boxing",
                TrainingType.CARDIO, LocalDateTime.now(), Duration.ofHours(1));


        when(traineeService.getTraineeById(traineeEntity.getUserId())).thenReturn(new TraineeDto());
        when(trainerService.getTrainerById(trainerId)).thenReturn(null);

        assertThatThrownBy(() -> trainingService.createTraining(trainingEntity))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainer with id: " + trainingEntity.getTrainerId());

        verify(trainerService, times(1)).getTrainerById(trainerId);
    }

    @Test
    public void testCreateTrainingSuccess(){
        // Arrange
        long traineeId = 1L;
        long trainerId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTraineeId(traineeId);
        trainingEntity.setTrainerId(trainerId);

        TraineeDto traineeDto = new TraineeDto();
        TrainerDto trainerDto = new TrainerDto();

        when(traineeService.getTraineeById(traineeId)).thenReturn(traineeDto);
        when(trainerService.getTrainerById(trainerId)).thenReturn(trainerDto);

        trainingService.createTraining(trainingEntity);

        verify(trainingDao, times(1)).createTraining(trainingEntity);
        verify(saveDataToFile, times(1)).writeMapToFile("Training");
    }










}
