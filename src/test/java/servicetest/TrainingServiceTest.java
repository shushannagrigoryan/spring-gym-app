package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.repository.TrainingRepository;
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
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    public void testCreateTrainingSuccess() {
        //given
        long traineeId = 1L;
        long trainerId = 1L;
        long trainingId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTraineeId(traineeId);
        trainingEntity.setTrainerId(trainerId);
        trainingEntity.setTrainingTypeId(trainingId);

        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setId(trainingId);

        when(traineeService.getTraineeById(traineeId)).thenReturn(new TraineeEntity());
        when(trainerService.getTrainerById(trainerId)).thenReturn(new TrainerEntity());
        when(trainingTypeService.getTrainingTypeById(trainingId)).thenReturn(trainingType);
        doNothing().when(trainingRepository).createTraining(trainingEntity);

        //when
        trainingService.createTraining(trainingEntity);

        //then
        verify(trainingRepository).createTraining(trainingEntity);
    }

    @Test
    public void testGetTrainingByIdSuccess() {
        //given
        Long trainingId = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setId(trainingId);

        when(trainingRepository.getTrainingById(trainingId)).thenReturn(trainingEntity);

        //when
        trainingService.getTrainingById(trainingId);

        //then
        verify(trainingRepository).getTrainingById(trainingId);
    }

    @Test
    public void testGetTrainingByIdInvalidId() {
        //given
        Long trainingId = 1L;
        when(trainingRepository.getTrainingById(trainingId)).thenReturn(null);

        //then
        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
                () -> trainingService.getTrainingById(trainingId));
        assertEquals(String.format("No training with id: %d", trainingId), exception.getMessage());

        verify(trainingRepository).getTrainingById(trainingId);
    }

    @Test
    public void testUpdateTrainingSuccess() {
        // Given
        Long trainingId = 1L;
        TrainingEntity training = new TrainingEntity();
        training.setId(trainingId);
        training.setTrainingName("training1");

        TrainingEntity updatedTraining = new TrainingEntity();
        updatedTraining.setTrainingName("training2");
        updatedTraining.setTrainingTypeId(2L);
        updatedTraining.setTrainingDate(LocalDate.of(2023, 9, 1));
        updatedTraining.setTrainingDuration(BigDecimal.valueOf(60));
        updatedTraining.setTraineeId(1L);
        updatedTraining.setTrainerId(1L);

        when(trainingRepository.getTrainingById(trainingId)).thenReturn(training);
        when(trainingTypeService.getTrainingTypeById(updatedTraining.getTrainingTypeId()))
                .thenReturn(new TrainingTypeEntity());
        when(traineeService.getTraineeById(updatedTraining.getTraineeId())).thenReturn(new TraineeEntity());
        when(trainerService.getTrainerById(updatedTraining.getTrainerId())).thenReturn(new TrainerEntity());

        // When
        trainingService.updateTraining(trainingId, updatedTraining);

        // Then
        assertEquals("training2", training.getTrainingName());
        verify(trainingRepository).updateTraining(training);
    }

    @Test
    public void testUpdateTrainingInvalidId() {
        //given
        long trainingId = 1L;
        when(trainingRepository.getTrainingById(trainingId)).thenReturn(null);

        //then
        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
                () -> trainingService.updateTraining(trainingId, new TrainingEntity()));
        assertEquals(String.format("No training with id: %d", trainingId), exception.getMessage());
    }


}
