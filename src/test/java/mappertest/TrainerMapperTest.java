//package mappertest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//import org.example.dto.TrainerDto;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingTypeEntity;
//import org.example.entity.UserEntity;
//import org.example.mapper.TrainerMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerMapperTest {
//    @InjectMocks
//    private TrainerMapper trainerMapper;
//
//    @Test
//    public void testEntityToDtoNull() {
//        assertNull(trainerMapper.entityToDto(null));
//    }
//
//    @Test
//    public void testEntityToDto() {
//        //given
//        TrainerEntity trainerEntity = new TrainerEntity();
//        UserEntity user = new UserEntity();
//        user.setFirstName("Jack");
//        user.setLastName("Smith");
//        trainerEntity.setUser(user);
//        TrainingTypeEntity specialization = new TrainingTypeEntity();
//        specialization.setId(1L);
//        trainerEntity.setSpecialization(specialization);
//
//        //when
//        TrainerDto trainerDto = trainerMapper.entityToDto(trainerEntity);
//
//        //then
//        assertNotNull(trainerDto);
//        assertEquals(trainerEntity.getUser().getFirstName(), trainerDto.getFirstName());
//        assertEquals(trainerEntity.getUser().getLastName(), trainerDto.getLastName());
//        assertEquals(trainerEntity.getSpecialization().getId(), trainerDto.getSpecialization());
//    }
//
//    @Test
//    public void testDtoToEntityNull() {
//        assertNull(trainerMapper.dtoToEntity(null));
//    }
//
//    @Test
//    public void testDtoToEntity() {
//        //given
//        TrainerDto trainerDto =
//                new TrainerDto("Jack", "Smith", 1L);
//
//        //when
//        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
//
//        //then
//        assertNotNull(trainerEntity);
//        assertEquals(trainerDto.getFirstName(), trainerEntity.getUser().getFirstName());
//        assertEquals(trainerDto.getLastName(), trainerEntity.getUser().getLastName());
//        assertEquals(trainerDto.getSpecialization(), trainerEntity.getSpecializationId());
//    }
//}
