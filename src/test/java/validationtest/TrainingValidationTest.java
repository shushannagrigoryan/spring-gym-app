package validationtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.dto.TrainingDto;
import org.example.validation.TrainingValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingValidationTest {
    @InjectMocks
    private TrainingValidation trainingValidation;

    @Test
    public void testValidateTrainingSuccess() {
        //given
        TrainingDto trainingDto = new TrainingDto(1L, 1L,
                "trainingName", 1L, LocalDate.now(), BigDecimal.valueOf(60));

        //then
        assertDoesNotThrow(() -> trainingValidation.validateTraining(trainingDto));
    }

    @Test
    public void testValidateTrainerNullTrainingName() {
        //given
        TrainingDto trainingDto = new TrainingDto(1L, 1L,
                null, 1L, LocalDate.now(), BigDecimal.valueOf(60));

        //then
        assertThrows(RuntimeException.class,
                () -> trainingValidation.validateTraining(trainingDto),
                "Training name is required.");
    }

    @Test
    public void testValidateTrainerNullTrainingDate() {
        //given
        TrainingDto trainingDto = new TrainingDto(1L, 1L,
                "trainingName", 1L, null, BigDecimal.valueOf(60));

        //then
        assertThrows(RuntimeException.class,
                () -> trainingValidation.validateTraining(trainingDto), "Training date is required.");
    }

    @Test
    public void testValidateTrainerNullTrainingDuration() {
        //given
        TrainingDto trainingDto = new TrainingDto(1L, 1L,
                "trainingName", 1L, LocalDate.now(), null);

        //then
        assertThrows(RuntimeException.class,
                () -> trainingValidation.validateTraining(trainingDto), "Training duration is required.");
    }


}
