package controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import org.example.controller.TrainingTypeController;
import org.example.dto.responsedto.ResponseDto;
import org.example.dto.responsedto.TrainingTypeResponseDto;
import org.example.entity.TrainingTypeEntity;
import org.example.mapper.TrainingTypeMapper;
import org.example.metrics.TrainingTypeRequestMetrics;
import org.example.services.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeControllerTest {
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private TrainingTypeMapper trainingTypeMapper;
    @Mock
    private TrainingTypeRequestMetrics trainingTypeRequestMetrics;
    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Test
    public void testGetTrainingTypes() {
        //given
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        doNothing().when(trainingTypeRequestMetrics).incrementCounter();
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(List.of(trainingTypeEntity));
        TrainingTypeResponseDto responseDto = new TrainingTypeResponseDto();
        when(trainingTypeMapper.entityToResponseDto(trainingTypeEntity)).thenReturn(responseDto);

        //when
        ResponseEntity<ResponseDto<List<TrainingTypeResponseDto>>> result =
            trainingTypeController.getTrainingTypes();

        //then
        assertNotNull(result);
        assertEquals(1, Objects.requireNonNull(result.getBody()).getPayload().size());
        assertEquals(responseDto, Objects.requireNonNull(result.getBody()).getPayload().get(0));
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(trainingTypeService).getAllTrainingTypes();
        verify(trainingTypeMapper).entityToResponseDto(trainingTypeEntity);
    }


}
