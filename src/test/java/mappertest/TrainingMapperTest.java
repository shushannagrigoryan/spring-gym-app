//package mappertest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.mockito.Mockito.when;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import org.example.dto.TrainingDto;
//import org.example.dto.responsedto.TraineeCriteriaTrainingsResponseDto;
//import org.example.dto.responsedto.TrainerCriteriaTrainingsResponseDto;
//import org.example.dto.responsedto.TrainingTypeResponseDto;
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingEntity;
//import org.example.entity.TrainingTypeEntity;
//import org.example.entity.UserEntity;
//import org.example.mapper.TrainingMapper;
//import org.example.mapper.TrainingTypeMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainingMapperTest {
//    @Mock
//    private TrainingTypeMapper trainingTypeMapper;
//    @InjectMocks
//    private TrainingMapper trainingMapper;
//
//    @Test
//    public void testEntityToDtoNull() {
//        assertNull(trainingMapper.entityToDto(null));
//    }
//
//    @Test
//    public void testEntityToDto() {
//        //given
//        TrainingEntity trainingEntity =
//                new TrainingEntity(1L, 1L,
//                        "boxing", 1L,
//                        LocalDateTime.now(), BigDecimal.valueOf(60));
//        TraineeEntity traineeEntity = new TraineeEntity();
//        traineeEntity.setId(1L);
//        TrainerEntity trainerEntity = new TrainerEntity();
//        trainerEntity.setId(1L);
//        trainingEntity.setTrainer(trainerEntity);
//        trainingEntity.setTrainee(traineeEntity);
//        TrainingTypeEntity trainingType = new TrainingTypeEntity();
//        trainingType.setId(1L);
//        trainingEntity.setTrainingType(trainingType);
//
//        //when
//        TrainingDto trainingDto = trainingMapper.entityToDto(trainingEntity);
//
//        //then
//        assertNotNull(trainingDto);
//        assertEquals(trainingEntity.getTraineeId(), trainingDto.getTraineeId());
//        assertEquals(trainingEntity.getTrainerId(), trainingDto.getTrainerId());
//        assertEquals(trainingEntity.getTrainingName(), trainingDto.getTrainingName());
//        assertEquals(trainingEntity.getTrainingDate(), trainingDto.getTrainingDate());
//        assertEquals(trainingEntity.getTrainingDuration(), trainingDto.getTrainingDuration());
//    }
//
//    @Test
//    public void testDtoToEntityNull() {
//        assertNull(trainingMapper.dtoToEntity(null));
//    }
//
//    @Test
//    public void testDtoToEntity() {
//        //given
//        TrainingDto trainingDto =
//                new TrainingDto(1L, 1L,
//                        "boxing", 1L,
//                        LocalDateTime.now(), BigDecimal.valueOf(60));
//
//        //when
//        TrainingEntity trainingEntity = trainingMapper.dtoToEntity(trainingDto);
//
//        //then
//        assertNotNull(trainingDto);
//        assertEquals(trainingDto.getTraineeId(), trainingEntity.getTraineeId());
//        assertEquals(trainingDto.getTrainerId(), trainingEntity.getTrainerId());
//        assertEquals(trainingDto.getTrainingName(), trainingEntity.getTrainingName());
//        assertEquals(trainingDto.getTrainingDate(), trainingEntity.getTrainingDate());
//        assertEquals(trainingDto.getTrainingDuration(), trainingEntity.getTrainingDuration());
//    }
//
//    @Test
//    public void testTraineeTrainingsEntityToCriteriaDto() {
//        //given
//        TrainingEntity training = new TrainingEntity(1L, 1L, "trainingName",
//                1L, LocalDateTime.now(), BigDecimal.valueOf(60));
//        TrainerEntity trainer = new TrainerEntity();
//        trainer.setUser(new UserEntity());
//        training.setTrainer(trainer);
//        when(trainingTypeMapper.entityToResponseDto(training.getTrainingType()))
//                .thenReturn(new TrainingTypeResponseDto());
//        //when
//        TraineeCriteriaTrainingsResponseDto responseDto =
//                trainingMapper.traineeTrainingsEntityToCriteriaDto(training);
//
//        //then
//        assertNotNull(responseDto);
//        assertEquals("trainingName", responseDto.getTrainingName());
//    }
//
//
//    @Test
//    public void testNullTraineeTrainingsEntityToCriteriaDto() {
//        assertNull(trainingMapper.traineeTrainingsEntityToCriteriaDto(null));
//    }
//
//    @Test
//    public void testTrainerTrainingsEntityToCriteriaDto() {
//        //given
//        TrainingEntity training = new TrainingEntity(1L, 1L, "trainingName",
//                1L, LocalDateTime.now(), BigDecimal.valueOf(60));
//        TraineeEntity trainee = new TraineeEntity();
//        trainee.setUser(new UserEntity());
//        training.setTrainee(trainee);
//        when(trainingTypeMapper.entityToResponseDto(training.getTrainingType()))
//                .thenReturn(new TrainingTypeResponseDto());
//        //when
//        TrainerCriteriaTrainingsResponseDto responseDto =
//                trainingMapper.trainerTrainingsEntityToCriteriaDto(training);
//
//        //then
//        assertNotNull(responseDto);
//        assertEquals("trainingName", responseDto.getTrainingName());
//    }
//
//
//    @Test
//    public void testNullTrainerTrainingsEntityToCriteriaDto() {
//        assertNull(trainingMapper.trainerTrainingsEntityToCriteriaDto(null));
//    }
//
//
//}
