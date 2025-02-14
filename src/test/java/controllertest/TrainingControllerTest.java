package controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import org.example.controller.TrainingController;
import org.example.dto.requestdto.TraineeTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainerTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainingCreateRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.dto.responsedto.TraineeCriteriaTrainingsResponseDto;
import org.example.dto.responsedto.TrainerCriteriaTrainingsResponseDto;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.example.metrics.TrainingRequestMetrics;
import org.example.services.TrainingService;
import org.example.services.UpdateWorkloadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {
    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainingRequestMetrics trainingRequestMetrics;
    @Mock
    private UpdateWorkloadService updateWorkloadService;

    @InjectMocks
    private TrainingController trainingController;

    @Test
    public void testCreateTraining() {
        //given
        TrainingCreateRequestDto requestDto = new TrainingCreateRequestDto();
        TrainingEntity training = new TrainingEntity();
        doNothing().when(trainingRequestMetrics).incrementCounter();
        when(trainingService.createTraining(requestDto)).thenReturn(training);
        doNothing().when(updateWorkloadService).confirmWorkloadUpdate(training);

        //when
        ResponseEntity<ResponseDto<Object>> result = trainingController.createTraining(requestDto);

        //then
        verify(trainingService).createTraining(requestDto);
        assertEquals("Successfully created a new training.",
            Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getTraineeTrainingsFilter() {
        //given
        String traineeUsername = "A.A";
        String fromDate = "2024-01-02T00:00";
        String toDate = "2024-08-02T00:00";
        String trainerUsername = "B.B";
        String trainingType = "1";
        TraineeTrainingsFilterRequestDto requestDto = new TraineeTrainingsFilterRequestDto(
            fromDate, toDate, trainerUsername, trainingType);
        TrainingEntity training = new TrainingEntity();
        doNothing().when(trainingRequestMetrics).incrementCounter();
        when(trainingService.getTraineeTrainingsByFilter(traineeUsername, requestDto)).thenReturn(List.of(training));
        TraineeCriteriaTrainingsResponseDto responseDto = new TraineeCriteriaTrainingsResponseDto();
        when(trainingMapper.traineeTrainingsEntityToCriteriaDto(training)).thenReturn(responseDto);

        //when
        ResponseEntity<ResponseDto<List<TraineeCriteriaTrainingsResponseDto>>> result =
            trainingController.getTraineeTrainingsFilter(traineeUsername, requestDto);

        //then
        verify(trainingService).getTraineeTrainingsByFilter(traineeUsername, requestDto);
        verify(trainingMapper).traineeTrainingsEntityToCriteriaDto(training);
        assertEquals(responseDto, Objects.requireNonNull(result.getBody()).getPayload().get(0));
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    public void getTrainerTrainingsFilter() {
        //given
        String traineeUsername = "A.A";
        String fromDate = "2024-01-02T00:00";
        String toDate = "2024-08-02T00:00";
        String trainerUsername = "B.B";
        TrainerTrainingsFilterRequestDto requestDto = new TrainerTrainingsFilterRequestDto(
            fromDate, toDate, traineeUsername);
        TrainingEntity training = new TrainingEntity();
        doNothing().when(trainingRequestMetrics).incrementCounter();
        when(trainingService.getTrainerTrainingsByFilter(trainerUsername, requestDto)).thenReturn(List.of(training));
        TrainerCriteriaTrainingsResponseDto responseDto = new TrainerCriteriaTrainingsResponseDto();
        when(trainingMapper.trainerTrainingsEntityToCriteriaDto(training)).thenReturn(responseDto);

        //when
        ResponseEntity<ResponseDto<List<TrainerCriteriaTrainingsResponseDto>>> result =
            trainingController.getTrainerTrainingsFilter(trainerUsername, requestDto);

        //then
        verify(trainingService).getTrainerTrainingsByFilter(trainerUsername, requestDto);
        verify(trainingMapper).trainerTrainingsEntityToCriteriaDto(training);
        assertEquals(responseDto, Objects.requireNonNull(result.getBody()).getPayload().get(0));
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

}
