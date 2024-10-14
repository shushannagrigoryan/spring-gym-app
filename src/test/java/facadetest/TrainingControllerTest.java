package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.example.controller.TrainingController;
import org.example.dto.requestdto.TraineeTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainerTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainingCreateRequestDto;
import org.example.dto.responsedto.TraineeCriteriaTrainingsResponseDto;
import org.example.dto.responsedto.TrainerCriteriaTrainingsResponseDto;
import org.example.entity.TrainingEntity;
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
        ResponseEntity<String> result =  trainingController.createTraining(requestDto);

        //then
        verify(trainingService).createTraining(requestDto);
        assertEquals("Successfully created a new training.", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    //    @Test
    //    public void testGetTrainingByIdSuccess() {
    //        // Given
    //        Long id = 1L;
    //        TrainingEntity trainingEntity = new TrainingEntity();
    //        TrainingDto expectedTrainingDto = new TrainingDto();
    //        when(trainingService.getTrainingById(id)).thenReturn(trainingEntity);
    //        when(trainingMapper.entityToDto(trainingEntity)).thenReturn(expectedTrainingDto);
    //
    //        // When
    //        TrainingDto actualTrainingDto = trainingFacade.getTrainingById(id);
    //
    //        // Then
    //        verify(trainingService).getTrainingById(id);
    //        verify(trainingMapper).entityToDto(trainingEntity);
    //        assertEquals(expectedTrainingDto, actualTrainingDto);
    //
    //    }

    //    @Test
    //    public void testUpdateTraining() {
    //        // Given
    //        Long trainingId = 1L;
    //        TrainingDto trainingDto = new TrainingDto();
    //        TrainingEntity trainingEntity = new TrainingEntity();
    //        when(trainingMapper.dtoToEntity(trainingDto)).thenReturn(trainingEntity);
    //
    //        // When
    //        trainingFacade.updateTraining(trainingId, trainingDto);
    //
    //        // Then
    //        verify(trainingMapper).dtoToEntity(trainingDto);
    //        verify(trainingService).updateTraining(trainingId, trainingEntity);
    //    }

    //    @Test
    //    public void testGetTraineeTrainingsByFilter() {
    //        // Given
    //        String traineeUsername = "R.R";
    //        LocalDate fromDate = LocalDate.now();
    //        LocalDate toDate = LocalDate.now();
    //        Long trainingTypeId = 1L;
    //        String trainerUsername = "O.O";
    //
    //        List<TrainingEntity> trainingList = Arrays.asList(new TrainingEntity(), new TrainingEntity());
    //        when(trainingService.getTraineeTrainingsByFilter(
    //                traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername))
    //                .thenReturn(trainingList);
    //
    //        List<TrainingDto> expectedDtoList = Arrays.asList(new TrainingDto(), new TrainingDto());
    //        when(trainingMapper.entityToDto(trainingList.get(0))).thenReturn(expectedDtoList.get(0));
    //        when(trainingMapper.entityToDto(trainingList.get(1))).thenReturn(expectedDtoList.get(1));
    //
    //        // When
    //        List<TrainingDto> actualDtoList = trainingFacade.getTraineeTrainingsByFilter(
    //                traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername);
    //
    //        // Then
    //        verify(trainingService)
    //                .getTraineeTrainingsByFilter(traineeUsername, fromDate, toDate,
    //                        trainingTypeId, trainerUsername);
    //        verify(trainingMapper, times(2)).entityToDto(any(TrainingEntity.class));
    //        assertEquals(expectedDtoList, actualDtoList);
    //    }

    //    @Test
    //    public void testGetTrainerTrainingsByFilter() {
    //        // Given
    //        String trainerUsername = "R.R";
    //        LocalDate fromDate = LocalDate.now();
    //        LocalDate toDate = LocalDate.now();
    //        String traineeUsername = "O.O";
    //
    //        List<TrainingEntity> trainingList = Arrays.asList(new TrainingEntity(), new TrainingEntity());
    //        when(trainingService.getTrainerTrainingsByFilter(trainerUsername, fromDate, toDate, traineeUsername))
    //                .thenReturn(trainingList);
    //
    //        List<TrainingDto> expectedDtoList = Arrays.asList(new TrainingDto(), new TrainingDto());
    //        when(trainingMapper.entityToDto(trainingList.get(0))).thenReturn(expectedDtoList.get(0));
    //        when(trainingMapper.entityToDto(trainingList.get(1))).thenReturn(expectedDtoList.get(1));
    //
    //        // When
    //        List<TrainingDto> actualDtoList = trainingFacade.getTrainerTrainingsByFilter(
    //                trainerUsername, fromDate, toDate, traineeUsername);
    //
    //        // Then
    //        verify(trainingService).getTrainerTrainingsByFilter(trainerUsername, fromDate, toDate, traineeUsername);
    //        verify(trainingMapper, times(2)).entityToDto(any(TrainingEntity.class));
    //        assertEquals(expectedDtoList, actualDtoList);
    //    }

    @Test
    public void getTraineeTrainingsFilter() {
        //given
        String traineeUsername = "A.A";
        LocalDate fromDate = LocalDate.of(2024, 1, 2);
        LocalDate toDate = LocalDate.of(2024, 8, 2);
        String trainerUsername = "B.B";
        Long trainingType = 1L;
        TraineeTrainingsFilterRequestDto requestDto = new TraineeTrainingsFilterRequestDto(
                traineeUsername, fromDate, toDate, trainerUsername, trainingType);
        TrainingEntity training = new TrainingEntity();
        when(trainingService.getTraineeTrainingsByFilter(requestDto)).thenReturn(List.of(training));
        TraineeCriteriaTrainingsResponseDto responseDto = new TraineeCriteriaTrainingsResponseDto();
        when(trainingMapper.traineeTrainingsEntityToCriteriaDto(training)).thenReturn(responseDto);

        //when
        ResponseEntity<List<TraineeCriteriaTrainingsResponseDto>> result =
                trainingController.getTraineeTrainingsFilter(requestDto);

        //then
        verify(trainingService).getTraineeTrainingsByFilter(requestDto);
        verify(trainingMapper).traineeTrainingsEntityToCriteriaDto(training);
        assertEquals(responseDto, Objects.requireNonNull(result.getBody()).get(0));
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    public void getTrainerTrainingsFilter() {
        //given
        String traineeUsername = "A.A";
        LocalDate fromDate = LocalDate.of(2024, 1, 2);
        LocalDate toDate = LocalDate.of(2024, 8, 2);
        String trainerUsername = "B.B";
        TrainerTrainingsFilterRequestDto requestDto = new TrainerTrainingsFilterRequestDto(
                trainerUsername, fromDate, toDate, traineeUsername);
        TrainingEntity training = new TrainingEntity();
        when(trainingService.getTrainerTrainingsByFilter(requestDto)).thenReturn(List.of(training));
        TrainerCriteriaTrainingsResponseDto responseDto = new TrainerCriteriaTrainingsResponseDto();
        when(trainingMapper.trainerTrainingsEntityToCriteriaDto(training)).thenReturn(responseDto);

        //when
        ResponseEntity<List<TrainerCriteriaTrainingsResponseDto>> result =
                trainingController.getTrainerTrainingsFilter(requestDto);

        //then
        verify(trainingService).getTrainerTrainingsByFilter(requestDto);
        verify(trainingMapper).trainerTrainingsEntityToCriteriaDto(training);
        assertEquals(responseDto, Objects.requireNonNull(result.getBody()).get(0));
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

}
