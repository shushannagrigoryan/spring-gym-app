//package validationtest;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import org.example.dto.TrainerDto;
//import org.example.validation.TrainerValidation;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerValidationTest {
//
//    @InjectMocks
//    private TrainerValidation trainerValidation;
//
//    @Test
//    public void testValidateTraineeSuccess() {
//        //given
//        TrainerDto trainerDto = new TrainerDto("A", "B", 1L);
//
//        //then
//        assertDoesNotThrow(() -> trainerValidation.validateTrainer(trainerDto));
//    }
//
//    @Test
//    public void testValidateTrainerNullFirstName() {
//        //given
//        TrainerDto trainerDto = new TrainerDto(null, "B", 1L);
//
//        //then
//        assertThrows(RuntimeException.class,
//                () -> trainerValidation.validateTrainer(trainerDto),
//                "Trainer firstName is required.");
//    }
//
//    @Test
//    public void testValidateTrainerNullLastName() {
//        //given
//        TrainerDto trainerDto = new TrainerDto("A", null, 1L);
//
//        //then
//        assertThrows(RuntimeException.class,
//                () -> trainerValidation.validateTrainer(trainerDto), "Trainer lastName is required.");
//    }
//
//
//}
