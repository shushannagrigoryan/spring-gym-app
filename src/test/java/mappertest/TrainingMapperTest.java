//package mappertest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import org.example.dto.TrainingDto;
//import org.example.entity.TrainingEntity;
//import org.example.entity.TrainingType;
//import org.example.mapper.TrainingMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainingMapperTest {
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
//                        "boxing", TrainingType.CARDIO,
//                        LocalDateTime.now(), Duration.ofHours(1));
//
//        //when
//        TrainingDto trainingDto = trainingMapper.entityToDto(trainingEntity);
//
//        //then
//        assertNotNull(trainingDto);
//        assertEquals(trainingEntity.getTraineeId(), trainingDto.getTraineeId());
//        assertEquals(trainingEntity.getTrainerId(), trainingDto.getTrainerId());
//        assertEquals(trainingEntity.getTrainingName(), trainingDto.getTrainingName());
//        assertEquals(trainingEntity.getTrainingType(), trainingDto.getTrainingType());
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
//                        "boxing", TrainingType.CARDIO,
//                        LocalDateTime.now(), Duration.ofHours(1));
//
//        //when
//        TrainingEntity trainingEntity = trainingMapper.dtoToEntity(trainingDto);
//
//        assertNotNull(trainingDto);
//        assertEquals(trainingDto.getTraineeId(), trainingEntity.getTraineeId());
//        assertEquals(trainingDto.getTrainerId(), trainingEntity.getTrainerId());
//        assertEquals(trainingDto.getTrainingName(), trainingEntity.getTrainingName());
//        assertEquals(trainingDto.getTrainingType(), trainingEntity.getTrainingType());
//        assertEquals(trainingDto.getTrainingDate(), trainingEntity.getTrainingDate());
//        assertEquals(trainingDto.getTrainingDuration(), trainingEntity.getTrainingDuration());
//    }
//}
