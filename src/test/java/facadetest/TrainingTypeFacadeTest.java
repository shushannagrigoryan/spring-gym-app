package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.dto.TrainingTypeDto;
import org.example.entity.TrainingTypeEntity;
import org.example.facade.TrainingTypeFacade;
import org.example.mapper.TrainingTypeMapper;
import org.example.services.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeFacadeTest {
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private TrainingTypeMapper trainingTypeMapper;
    @InjectMocks
    private TrainingTypeFacade trainingTypeFacade;

    @Test
    public void testGetTrainingTypeById() {
        // Given
        Long trainingTypeId = 1L;
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
        when(trainingTypeService.getTrainingTypeById(trainingTypeId)).thenReturn(trainingTypeEntity);
        when(trainingTypeMapper.entityToDto(trainingTypeEntity)).thenReturn(trainingTypeDto);

        // When
        TrainingTypeDto result = trainingTypeFacade.getTrainingTypeById(trainingTypeId);

        // Then
        assertNotNull(result);
        assertEquals(trainingTypeDto, result);
        verify(trainingTypeService).getTrainingTypeById(trainingTypeId);
        verify(trainingTypeMapper).entityToDto(trainingTypeEntity);
    }


}
