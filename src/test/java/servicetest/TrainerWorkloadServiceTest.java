package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import org.example.controller.TrainerWorkloadClient;
import org.example.dto.requestdto.ActionType;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.example.dto.responsedto.GetTrainerWorkloadResponseDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainerWorkloadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadServiceTest {
    @Mock
    private ResponseEntity<ResponseDto<String>> responseEntity;
    @Mock
    private ResponseEntity<ResponseDto<BigDecimal>> responseWorkload;
    @Mock
    private ResponseDto<String> responseDto;
    @Mock
    private TrainingMapper trainingMapper;
    @Mock
    private TrainerWorkloadClient trainerWorkloadClient;
    @InjectMocks
    private TrainerWorkloadService trainerWorkloadService;

    @Test
    public void testUpdateTrainerWorkload() {
        //given
        TrainingEntity trainingEntity = new TrainingEntity();
        UpdateTrainerWorkloadRequestDto updateTrainerWorkloadRequestDto = new UpdateTrainerWorkloadRequestDto();
        doReturn(updateTrainerWorkloadRequestDto)
            .when(trainingMapper).getTrainerWorkloadRequestDto(trainingEntity, ActionType.ADD);
        doReturn(responseEntity).when(trainerWorkloadClient).updateWorkload(updateTrainerWorkloadRequestDto);
        doReturn(responseDto).when(responseEntity).getBody();

        //when
        trainerWorkloadService.updateTrainerWorkload(trainingEntity, ActionType.ADD);

        //then
        verify(trainingMapper).getTrainerWorkloadRequestDto(trainingEntity, ActionType.ADD);
        verify(trainerWorkloadClient).updateWorkload(updateTrainerWorkloadRequestDto);
        verify(responseEntity, times(2)).getBody();
        verify(responseDto).getPayload();
    }

    @Test
    public void testFallbackMethodForUpdateWorkload() {
        assertThrows(RuntimeException.class, () -> trainerWorkloadService.fallbackMethodForUpdateWorkload(
            new TrainingEntity(), ActionType.ADD, new Throwable()
        ), "Trainer workload service is currently not available.");
    }

    @Test
    public void test() {
        assertThrows(RuntimeException.class, () -> trainerWorkloadService.fallbackMethodForGetWorkload(
            new TrainerWorkloadRequestDto(), new Throwable()
        ), "Trainer workload service is currently not available.");
    }

    @Test
    public void getTrainerWorkloadDurationNotPresent() {
        //given
        String username = "user";
        String year = "2024";
        String month = "7";
        BigDecimal duration = BigDecimal.valueOf(60);
        TrainerWorkloadRequestDto trainerWorkloadRequestDto = new TrainerWorkloadRequestDto(username, year, month);
        doReturn(responseWorkload).when(trainerWorkloadClient).getWorkload(username, year, month);
        doReturn(responseDto).when(responseWorkload).getBody();
        doReturn(duration).when(responseDto).getPayload();

        //when
        GetTrainerWorkloadResponseDto result = trainerWorkloadService.getTrainerWorkload(trainerWorkloadRequestDto);

        //then
        verify(trainerWorkloadClient).getWorkload(username, year, month);
        verify(responseWorkload, times(2)).getBody();
        verify(responseDto).getPayload();
        assertEquals(duration, result.getWorkload());
    }

    @Test
    public void getTrainerWorkloadDurationPresent() {
        //given
        String username = "user";
        String year = "2024";
        String month = "7";
        TrainerWorkloadRequestDto trainerWorkloadRequestDto = new TrainerWorkloadRequestDto(username, year, month);
        doReturn(responseWorkload).when(trainerWorkloadClient).getWorkload(username, year, month);
        doReturn(null).when(responseWorkload).getBody();

        //when
        GetTrainerWorkloadResponseDto result = trainerWorkloadService.getTrainerWorkload(trainerWorkloadRequestDto);

        //then
        verify(trainerWorkloadClient).getWorkload(username, year, month);
        verify(responseWorkload, times(1)).getBody();
        verify(responseDto, times(0)).getPayload();
        assertEquals(BigDecimal.ZERO, result.getWorkload());
    }
}
