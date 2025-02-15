package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.example.facade.TrainingFacade;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainingService;
import org.example.validation.TrainingValidation;
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
    @Mock
    private TrainingValidation trainingValidation;

    @InjectMocks
    private TrainingFacade trainingFacade;

    @Test
    public void testCreateTraining() {
        // Given
        TrainingDto trainingDto = new TrainingDto();
        TrainingEntity trainingEntity = new TrainingEntity();

        when(trainingMapper.dtoToEntity(trainingDto)).thenReturn(trainingEntity);

        // When
        trainingFacade.createTraining(trainingDto);

        // Then
        verify(trainingValidation).validateTraining(trainingDto);
        verify(trainingService).createTraining(trainingEntity);
    }

    @Test
    public void testGetTrainingByIdSuccess() {
        // Given
        Long id = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        TrainingDto expectedTrainingDto = new TrainingDto();
        when(trainingService.getTrainingById(id)).thenReturn(trainingEntity);
        when(trainingMapper.entityToDto(trainingEntity)).thenReturn(expectedTrainingDto);

        // When
        TrainingDto actualTrainingDto = trainingFacade.getTrainingById(id);

        // Then
        verify(trainingService).getTrainingById(id);
        verify(trainingMapper).entityToDto(trainingEntity);
        assertEquals(expectedTrainingDto, actualTrainingDto);

    }

    @Test
    public void testUpdateTraining() {
        // Given
        Long trainingId = 1L;
        TrainingDto trainingDto = new TrainingDto();
        TrainingEntity trainingEntity = new TrainingEntity();
        when(trainingMapper.dtoToEntity(trainingDto)).thenReturn(trainingEntity);

        // When
        trainingFacade.updateTraining(trainingId, trainingDto);

        // Then
        verify(trainingMapper).dtoToEntity(trainingDto);
        verify(trainingService).updateTraining(trainingId, trainingEntity);
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
        when(trainingService.getTraineeTrainingsByFilter(
                traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername))
                .thenReturn(trainingList);

        List<TrainingDto> expectedDtoList = Arrays.asList(new TrainingDto(), new TrainingDto());
        when(trainingMapper.entityToDto(trainingList.get(0))).thenReturn(expectedDtoList.get(0));
        when(trainingMapper.entityToDto(trainingList.get(1))).thenReturn(expectedDtoList.get(1));

        // When
        List<TrainingDto> actualDtoList = trainingFacade.getTraineeTrainingsByFilter(
                traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername);

        // Then
        verify(trainingService)
                .getTraineeTrainingsByFilter(traineeUsername, fromDate, toDate,
                        trainingTypeId, trainerUsername);
        verify(trainingMapper, times(2)).entityToDto(any(TrainingEntity.class));
        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    public void testGetTrainerTrainingsByFilter() {
        // Given
        String trainerUsername = "R.R";
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();
        String traineeUsername = "O.O";

        List<TrainingEntity> trainingList = Arrays.asList(new TrainingEntity(), new TrainingEntity());
        when(trainingService.getTrainerTrainingsByFilter(trainerUsername, fromDate, toDate, traineeUsername))
                .thenReturn(trainingList);

        List<TrainingDto> expectedDtoList = Arrays.asList(new TrainingDto(), new TrainingDto());
        when(trainingMapper.entityToDto(trainingList.get(0))).thenReturn(expectedDtoList.get(0));
        when(trainingMapper.entityToDto(trainingList.get(1))).thenReturn(expectedDtoList.get(1));

        // When
        List<TrainingDto> actualDtoList = trainingFacade.getTrainerTrainingsByFilter(
                trainerUsername, fromDate, toDate, traineeUsername);

        // Then
        verify(trainingService).getTrainerTrainingsByFilter(trainerUsername, fromDate, toDate, traineeUsername);
        verify(trainingMapper, times(2)).entityToDto(any(TrainingEntity.class));
        assertEquals(expectedDtoList, actualDtoList);
    }

}
