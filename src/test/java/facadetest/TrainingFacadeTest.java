package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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




}
