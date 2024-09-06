package servicetest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import org.example.SaveDataToFile;
import org.example.TrainingType;
import org.example.dao.TrainingDao;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.example.exceptions.IllegalIdException;
import org.example.mapper.TrainingMapper;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.services.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    public void testCreateTrainingInvalidTraineeId() {

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
    public void testCreateTrainingInvalidTrainerId() {

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
    public void testCreateTrainingSuccess() {
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

    @Test
    public void testGetTrainingByIdSuccess() {
        Long trainingId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        TrainingDto trainingDto = new TrainingDto();
        trainingEntity.setTrainingId(trainingId);

        when(trainingDao.getTrainingById(trainingId)).thenReturn(Optional.of(trainingEntity));
        when(trainingMapper.entityToDto(trainingEntity)).thenReturn(trainingDto);

        TrainingDto trainingDtoActual = trainingService.getTrainingById(trainingId);

        assertNotNull(trainingDto);
        assertEquals(trainingDto, trainingDtoActual);
        verify(trainingDao, times(1)).getTrainingById(trainingId);
        verify(trainingMapper, times(1)).entityToDto(trainingEntity);
    }

    @Test
    public void testGetTrainingByIdInvalidId() {
        Long trainingId = 1L;
        when(trainingDao.getTrainingById(trainingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.getTrainingById(trainingId))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No training with id: " + trainingId);

        verify(trainingDao, times(1)).getTrainingById(trainingId);
    }


}
