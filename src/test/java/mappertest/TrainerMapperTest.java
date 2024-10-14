package mappertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.example.dto.TrainerDto;
import org.example.dto.requestdto.TrainerCreateRequestDto;
import org.example.dto.requestdto.TrainerUpdateRequestDto;
import org.example.dto.responsedto.TraineeProfileTrainerResponseDto;
import org.example.dto.responsedto.TrainerProfileDto;
import org.example.dto.responsedto.TrainerResponseDto;
import org.example.dto.responsedto.TrainingTypeResponseDto;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingTypeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerMapperTest {
    @Mock
    private TrainingTypeMapper trainingTypeMapper;
    @InjectMocks
    private TrainerMapper trainerMapper;

    @Test
    public void testEntityToDtoNull() {
        assertNull(trainerMapper.entityToDto(null));
    }

    @Test
    public void testEntityToDto() {
        //given
        TrainerEntity trainerEntity = new TrainerEntity();
        UserEntity user = new UserEntity();
        user.setFirstName("Jack");
        user.setLastName("Smith");
        trainerEntity.setUser(user);
        TrainingTypeEntity specialization = new TrainingTypeEntity();
        specialization.setId(1L);
        trainerEntity.setSpecialization(specialization);

        //when
        TrainerDto trainerDto = trainerMapper.entityToDto(trainerEntity);

        //then
        assertNotNull(trainerDto);
        assertEquals(trainerEntity.getUser().getFirstName(), trainerDto.getFirstName());
        assertEquals(trainerEntity.getUser().getLastName(), trainerDto.getLastName());
        assertEquals(trainerEntity.getSpecialization().getId(), trainerDto.getSpecialization());
    }

    @Test
    public void testDtoToEntity() {
        //given
        TrainerDto trainerDto =
                new TrainerDto("Jack", "Smith", 1L);

        //when
        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);

        //then
        assertNotNull(trainerEntity);
        assertEquals(trainerDto.getFirstName(), trainerEntity.getUser().getFirstName());
        assertEquals(trainerDto.getLastName(), trainerEntity.getUser().getLastName());
        assertEquals(trainerDto.getSpecialization(), trainerEntity.getSpecializationId());
    }

    @Test
    public void testEntityToResponseDto() {
        //given
        TrainerEntity trainerEntity = new TrainerEntity();
        UserEntity user = new UserEntity();
        user.setFirstName("Jack");
        user.setLastName("Smith");
        trainerEntity.setUser(user);
        TrainingTypeEntity specialization = new TrainingTypeEntity();
        specialization.setId(1L);
        trainerEntity.setSpecialization(specialization);

        //when
        TrainerResponseDto trainerResponseDto = trainerMapper.entityToResponseDto(trainerEntity);

        //then
        assertNotNull(trainerResponseDto);
    }

    @Test
    public void testNullEntityToResponseDto() {
        assertNull(trainerMapper.entityToResponseDto(null));
    }

    @Test
    public void testCreateDtoToEntity() {
        //given
        TrainerCreateRequestDto trainerCreateDto = new TrainerCreateRequestDto(
                "A", "A", 1L);

        //when
        TrainerEntity trainer = trainerMapper.dtoToEntity(trainerCreateDto);

        //then
        assertNotNull(trainer);

    }

    @Test
    public void testEntityToProfileDto() {
        //given
        UserEntity user = new UserEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer.setSpecialization(new TrainingTypeEntity());

        //when
        TrainerProfileDto trainerResponseDto
                = trainerMapper.entityToProfileDto(trainer);

        //then
        assertNotNull(trainerResponseDto);
    }

    @Test
    public void testEntityToTraineeTrainerResponseDto() {
        //given
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(new UserEntity("A", "D", "A.B", "pass"));
        trainerEntity.setSpecialization(new TrainingTypeEntity());
        when(trainingTypeMapper
                .entityToResponseDto(trainerEntity.getSpecialization())).thenReturn(new TrainingTypeResponseDto());

        //when
        TraineeProfileTrainerResponseDto trainer = trainerMapper.entityToTraineeTrainerResponseDto(trainerEntity);

        //then
        assertNotNull(trainer);
        assertNotNull(trainer.getUsername());
    }

    @Test
    public void testNullEntityToTraineeTrainerResponseDto() {
        assertNull(trainerMapper.entityToTraineeTrainerResponseDto(null));
    }

    @Test
    public void updateNullDtoToEntity() {
        assertNull(trainerMapper.updateDtoToEntity(null));
    }

    @Test
    public void updateDtoToEntity() {
        //given
        TrainerUpdateRequestDto trainer =
                new TrainerUpdateRequestDto("A.B", "A",
                        "B", 1L, false);

        //when
        TrainerEntity trainerEntity = trainerMapper.updateDtoToEntity(trainer);

        //then
        assertNotNull(trainerEntity);
        assertEquals("A.B", trainerEntity.getUser().getUsername());
        assertEquals("A", trainerEntity.getUser().getFirstName());

    }
}
