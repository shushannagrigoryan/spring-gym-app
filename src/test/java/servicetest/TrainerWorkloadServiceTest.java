package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.dto.requestdto.ActionType;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.example.dto.responsedto.GetTrainerWorkloadResponseDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.example.services.GetWorkloadService;
import org.example.services.TrainerWorkloadSenderService;
import org.example.services.TrainerWorkloadService;
import org.example.services.UpdateTrainerWorkloadSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadServiceTest {
    @Mock
    private TrainingMapper trainingMapper;
    @Mock
    private UpdateTrainerWorkloadSenderService updateTrainerWorkloadSenderService;
    @Mock
    private GetWorkloadService getWorkloadService;
    @Mock
    private TrainerWorkloadSenderService trainerWorkloadSenderService;
    @InjectMocks
    private TrainerWorkloadService trainerWorkloadService;

    @Test
    public void testUpdateTrainerWorkload() {
        //given
        TrainingEntity trainingEntity = new TrainingEntity();
        UpdateTrainerWorkloadRequestDto updateTrainerWorkloadRequestDto = new UpdateTrainerWorkloadRequestDto();
        doReturn(updateTrainerWorkloadRequestDto)
            .when(trainingMapper).getTrainerWorkloadRequestDto(trainingEntity, ActionType.ADD);
        doNothing().when(updateTrainerWorkloadSenderService).send(updateTrainerWorkloadRequestDto);

        //when
        trainerWorkloadService.updateTrainerWorkload(trainingEntity, ActionType.ADD);

        //then
        verify(trainingMapper).getTrainerWorkloadRequestDto(trainingEntity, ActionType.ADD);
        verify(updateTrainerWorkloadSenderService).send(updateTrainerWorkloadRequestDto);
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
    public void getTrainerWorkload() {
        //given
        String username = "user";
        String year = "2024";
        String month = "7";
        TrainerWorkloadRequestDto trainerWorkloadRequestDto = new TrainerWorkloadRequestDto(username, year, month);
        GetTrainerWorkloadResponseDto trainerWorkloadResponseDto  = new GetTrainerWorkloadResponseDto(username, year, month);
        ResponseEntity<ResponseDto<GetTrainerWorkloadResponseDto>> response =
            ResponseEntity.ok(new ResponseDto<>(trainerWorkloadResponseDto, "Successfully retrieved trainer's workload"));
        doNothing().when(trainerWorkloadSenderService).send(trainerWorkloadRequestDto);
        when(getWorkloadService.getWorkload(trainerWorkloadRequestDto)).thenReturn(response);

        //when
        ResponseEntity<ResponseDto<GetTrainerWorkloadResponseDto>> result = trainerWorkloadService.getTrainerWorkload(trainerWorkloadRequestDto);

        //then
        verify(getWorkloadService).getWorkload(trainerWorkloadRequestDto);
        verify(trainerWorkloadSenderService).send(trainerWorkloadRequestDto);
        assertEquals(response.getBody(), result.getBody());
    }
}
