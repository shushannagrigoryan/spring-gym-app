package mappertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.example.dto.responsedto.TrainerProfileResponseDto;
import org.example.dto.responsedto.TrainerProfileTraineeResponseDto;
import org.example.dto.responsedto.TrainerUpdateResponseDto;
import org.example.dto.responsedto.TrainingTypeResponseDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainerProfileMapper;
import org.example.mapper.TrainingTypeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

public class TrainerProfileMapperTest {
    @Mock
    private TrainingTypeMapper trainingTypeMapper;
    @Mock
    private  TraineeMapper traineeMapper;

    @InjectMocks
    private TrainerProfileMapper trainerProfileMapper;

    @Test
    public void testEntityToProfileDto() {
        //given
        TrainerEntity trainerEntity = getTrainerEntity();
        TrainingTypeResponseDto typeResponseDto = new TrainingTypeResponseDto();
        typeResponseDto.setId(1L);
        when(trainingTypeMapper.entityToResponseDto(trainerEntity.getSpecialization())).thenReturn(typeResponseDto);


        //when
        TrainerProfileResponseDto trainerDto = trainerProfileMapper.entityToProfileDto(trainerEntity);

        //then
        assertNotNull(trainerDto);
        assertEquals(trainerEntity.getUser().getFirstName(), trainerDto.getFirstName());
        assertEquals(trainerEntity.getUser().getLastName(), trainerDto.getLastName());
        assertEquals(trainerEntity.getSpecialization().getId(), trainerDto.getSpecialization().getId());
    }

    private static TrainerEntity getTrainerEntity() {
        UserEntity user = new UserEntity("A", "B", "A.B", "password12");
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(user);
        TrainingTypeEntity specialization = new TrainingTypeEntity();
        specialization.setId(1L);
        trainerEntity.setSpecialization(specialization);
        trainerEntity.setId(1L);
        trainerEntity.setTrainees(Set.of(new TraineeEntity()));
        trainerEntity.setTrainings(List.of(new TrainingEntity()));
        return trainerEntity;
    }

    @Test
    public void testNullEntityToProfileDto() {
        assertNull(trainerProfileMapper.entityToProfileDto(null));
    }

    @Test
    public void testEntityToUpdateDto() {
        //given
        UserEntity user = new UserEntity("A", "B", "A.B", "password12");
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(user);
        TrainingTypeEntity specialization = new TrainingTypeEntity();
        specialization.setId(1L);
        trainerEntity.setSpecialization(specialization);
        trainerEntity.setId(1L);
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user1 = new UserEntity();
        trainee.setUser(user1);
        TrainingEntity training = new TrainingEntity();
        training.setTrainee(trainee);
        trainerEntity.setTrainings(List.of(training));
        TrainingTypeResponseDto typeResponseDto = new TrainingTypeResponseDto();
        typeResponseDto.setId(1L);
        when(traineeMapper.entityToTrainerTraineeResponseDto(trainee))
                .thenReturn(new TrainerProfileTraineeResponseDto());

        //when
        TrainerUpdateResponseDto result = trainerProfileMapper.entityToUpdatedDto(trainerEntity);

        //then
        verify(traineeMapper).entityToTrainerTraineeResponseDto(trainee);
        assertEquals("A.B", result.getUsername());


    }

    @Test
    public void testEntityToUpdatedDto() {
        assertNull(trainerProfileMapper.entityToUpdatedDto(null));
    }
}
