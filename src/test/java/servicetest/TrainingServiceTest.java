package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.example.dto.requestdto.TrainingCreateRequestDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.metrics.TrainingMetrics;
import org.example.repositories.TrainingRepository;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.services.TrainingService;
import org.example.services.TrainingTypeService;
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
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingMetrics trainingMetrics;

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    public void testCreateTrainingSuccess() {
        //given
        String traineeUsername = "A.A";
        String trainerUsername = "B.B";
        long trainingTypeId = 2L;


        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setId(trainingTypeId);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setSpecialization(trainingType);
        when(trainingTypeService.getTrainingTypeById(trainingTypeId)).thenReturn(trainingType);
        when(traineeService.getTraineeByUsername(traineeUsername)).thenReturn(new TraineeEntity());
        when(trainerService.getTrainerByUsername(trainerUsername)).thenReturn(trainer);

        String trainingName = "trainingName";
        when(trainingRepository.save(any(TrainingEntity.class))).thenReturn(new TrainingEntity());
        TrainingCreateRequestDto requestDto = new TrainingCreateRequestDto(traineeUsername, trainerUsername,
                trainingName, LocalDateTime.now(), BigDecimal.valueOf(60));
        doNothing().when(trainingMetrics).incrementCounter();

        //when
        trainingService.createTraining(requestDto);

        //then
        verify(trainingRepository).save(any(TrainingEntity.class));
        verify(traineeService).getTraineeByUsername(traineeUsername);
        verify(trainerService).getTrainerByUsername(trainerUsername);

    }

    @Test
    public void testGetTrainingByIdSuccess() {
        //given
        Long trainingId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setId(trainingId);

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(trainingEntity));

        //when
        TrainingEntity training = trainingService.getTrainingById(trainingId);

        //then
        verify(trainingRepository).findById(trainingId);
        assertEquals(trainingId, training.getId());
    }

    @Test
    public void testGetTrainingByIdInvalidId() {
        //given
        Long trainingId = 1L;
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());

        //then
        GymEntityNotFoundException exception = assertThrows(GymEntityNotFoundException.class,
                () -> trainingService.getTrainingById(trainingId));
        assertEquals(String.format(String.format("No training with id: %d", trainingId)), exception.getMessage());

        verify(trainingRepository).findById(trainingId);
    }

    //    @Test
    //    public void testGetTraineeTrainingsByFilter() {
    //        // Given
    //        String traineeUsername = "A.A";
    //        LocalDate fromDate = LocalDate.of(2023, 1, 1);
    //        LocalDate toDate = LocalDate.of(2023, 12, 31);
    //        Long trainingTypeId = 1L;
    //        String trainerUsername = "B.B";
    //        TraineeTrainingsFilterRequestDto requestDto = new TraineeTrainingsFilterRequestDto(
    //                traineeUsername, fromDate, toDate, trainerUsername, trainingTypeId);
    //
    //
    //        List<TrainingEntity> expectedTrainings = List.of(new TrainingEntity());
    //        when(trainingCriteriaRepository.getTraineeTrainingsByFilter(
    //                traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername))
    //                .thenReturn(expectedTrainings);
    //
    //        // When
    //        List<TrainingEntity> actualTrainings = trainingService.getTraineeTrainingsByFilter(requestDto);
    //
    //        // Then
    //        assertEquals(expectedTrainings, actualTrainings);
    //        verify(trainingCriteriaRepository).getTraineeTrainingsByFilter(
    //                traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername);
    //    }

    //    @Test
    //    public void testGetTrainerTrainingsByFilter() {
    //        // Given
    //        String trainerUsername = "A.A";
    //        LocalDate fromDate = LocalDate.of(2023, 1, 1);
    //        LocalDate toDate = LocalDate.of(2023, 12, 31);
    //        String traineeUsername = "B.B";
    //        TrainerTrainingsFilterRequestDto requestDto = new TrainerTrainingsFilterRequestDto(
    //                trainerUsername, fromDate, toDate, traineeUsername);
    //
    //        List<TrainingEntity> expectedTrainings = List.of(new TrainingEntity());
    //        when(trainingCriteriaRepository.getTrainerTrainingsByFilter(trainerUsername, fromDate,
    //                toDate, traineeUsername))
    //                .thenReturn(expectedTrainings);
    //
    //        // When
    //        List<TrainingEntity> actualTrainings = trainingService.getTrainerTrainingsByFilter(requestDto);
    //
    //        // Then
    //        assertEquals(expectedTrainings, actualTrainings);
    //        verify(trainingCriteriaRepository).getTrainerTrainingsByFilter(
    //                trainerUsername, fromDate, toDate, traineeUsername);
    //    }
}
