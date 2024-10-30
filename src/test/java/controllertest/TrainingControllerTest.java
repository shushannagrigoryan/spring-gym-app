package controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.Objects;
import org.example.controller.TrainingController;
import org.example.dto.requestdto.TrainingCreateRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainingService;
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

    @InjectMocks
    private TrainingController trainingController;

    @Test
    public void testCreateTraining() {
        //given
        TrainingCreateRequestDto requestDto = new TrainingCreateRequestDto();
        doNothing().when(trainingService).createTraining(requestDto);

        //when
        ResponseEntity<ResponseDto<Object>> result =  trainingController.createTraining(requestDto);

        //then
        verify(trainingService).createTraining(requestDto);
        assertEquals("Successfully created a new training.",
                Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    //    @Test
    //    public void getTraineeTrainingsFilter() {
    //        //given
    //        String traineeUsername = "A.A";
    //        LocalDate fromDate = LocalDate.of(2024, 1, 2);
    //        LocalDate toDate = LocalDate.of(2024, 8, 2);
    //        String trainerUsername = "B.B";
    //        Long trainingType = 1L;
    //        TraineeTrainingsFilterRequestDto requestDto = new TraineeTrainingsFilterRequestDto(
    //                traineeUsername, fromDate, toDate, trainerUsername, trainingType);
    //        TrainingEntity training = new TrainingEntity();
    //        when(trainingService.getTraineeTrainingsByFilter(requestDto)).thenReturn(List.of(training));
    //        TraineeCriteriaTrainingsResponseDto responseDto = new TraineeCriteriaTrainingsResponseDto();
    //        when(trainingMapper.traineeTrainingsEntityToCriteriaDto(training)).thenReturn(responseDto);
    //
    //        //when
    //        ResponseEntity<ResponseDto<List<TraineeCriteriaTrainingsResponseDto>>> result =
    //                trainingController.getTraineeTrainingsFilter(traineeUsername, requestDto);
    //
    //        //then
    //        verify(trainingService).getTraineeTrainingsByFilter(requestDto);
    //        verify(trainingMapper).traineeTrainingsEntityToCriteriaDto(training);
    //        assertEquals(responseDto, Objects.requireNonNull(result.getBody()).getPayload().get(0));
    //        assertEquals(HttpStatus.OK, result.getStatusCode());
    //
    //    }

    //    @Test
    //    public void getTrainerTrainingsFilter() {
    //        //given
    //        String traineeUsername = "A.A";
    //        LocalDate fromDate = LocalDate.of(2024, 1, 2);
    //        LocalDate toDate = LocalDate.of(2024, 8, 2);
    //        String trainerUsername = "B.B";
    //        TrainerTrainingsFilterRequestDto requestDto = new TrainerTrainingsFilterRequestDto(
    //                trainerUsername, fromDate, toDate, traineeUsername);
    //        TrainingEntity training = new TrainingEntity();
    //        when(trainingService.getTrainerTrainingsByFilter(requestDto)).thenReturn(List.of(training));
    //        TrainerCriteriaTrainingsResponseDto responseDto = new TrainerCriteriaTrainingsResponseDto();
    //        when(trainingMapper.trainerTrainingsEntityToCriteriaDto(training)).thenReturn(responseDto);
    //
    //        //when
    //        ResponseEntity<ResponseDto<List<TrainerCriteriaTrainingsResponseDto>>> result =
    //                trainingController.getTrainerTrainingsFilter(trainerUsername, requestDto);
    //
    //        //then
    //        verify(trainingService).getTrainerTrainingsByFilter(requestDto);
    //        verify(trainingMapper).trainerTrainingsEntityToCriteriaDto(training);
    //        assertEquals(responseDto, Objects.requireNonNull(result.getBody()).getPayload().get(0));
    //        assertEquals(HttpStatus.OK, result.getStatusCode());
    //
    //    }

}
