package mappertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import org.example.dto.TraineeDto;
import org.example.dto.requestdto.TraineeCreateRequestDto;
import org.example.dto.requestdto.TraineeUpdateRequestDto;
import org.example.dto.responsedto.TraineeResponseDto;
import org.example.dto.responsedto.TrainerProfileTraineeResponseDto;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.example.mapper.TraineeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeMapperTest {
    @InjectMocks
    private TraineeMapper traineeMapper;

    @Test
    public void testEntityToDtoNull() {
        assertNull(traineeMapper.entityToDto(null));
    }

    @Test
    public void testEntityToDto() {
        //given
        UserEntity user = new UserEntity("A", "B", "A.B", "password12");
        TraineeEntity traineeEntity = new TraineeEntity(1L, LocalDate.now(),
            "myAddress", user, new ArrayList<>(), new HashSet<>());


        //when
        TraineeDto traineeDto = traineeMapper.entityToDto(traineeEntity);

        //then
        assertNotNull(traineeDto);
        assertEquals(traineeEntity.getUser().getFirstName(), traineeDto.getFirstName());
        assertEquals(traineeEntity.getUser().getLastName(), traineeDto.getLastName());
        assertEquals(traineeEntity.getAddress(), traineeDto.getAddress());
        assertEquals(traineeEntity.getDateOfBirth(), traineeDto.getDateOfBirth());
    }

    @Test
    public void testDtoToEntity() {
        //given
        TraineeDto traineeDto =
            new TraineeDto("Jack", "Smith",
                LocalDate.now(), "myAddress");

        //when
        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);

        //then
        assertNotNull(traineeEntity);
        assertEquals(traineeDto.getFirstName(), traineeEntity.getUser().getFirstName());
        assertEquals(traineeDto.getLastName(), traineeEntity.getUser().getLastName());
        assertEquals(traineeDto.getAddress(), traineeEntity.getAddress());
        assertEquals(traineeDto.getDateOfBirth(), traineeEntity.getDateOfBirth());
    }

    @Test
    public void testEntityToResponseDto() {
        //given
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        TraineeEntity traineeEntity =
            new TraineeEntity(1L, LocalDate.now(), "myAddress",
                user, new ArrayList<>(), new HashSet<>());

        //when
        TraineeResponseDto responseDto = traineeMapper.entityToResponseDto(traineeEntity);

        //then
        assertNotNull(responseDto);
        assertNotNull(responseDto.getUsername());
        assertNotNull(responseDto.getPassword());
    }

    @Test
    public void testNullEntityToResponseDto() {
        //when
        TraineeResponseDto responseDto = traineeMapper.entityToResponseDto(null);

        //then
        assertNull(responseDto);
    }

    @Test
    public void testCreateDtoToEntity() {
        //given

        TraineeCreateRequestDto traineeCreateDto = new TraineeCreateRequestDto(
            "A", "B", LocalDate.now(), "myAddress");
        //when
        TraineeEntity responseDto = traineeMapper.dtoToEntity(traineeCreateDto);

        //then
        assertNotNull(responseDto);
    }

    @Test
    public void testNullEntityToTrainerTraineeResponseDto() {
        //when
        TrainerProfileTraineeResponseDto responseDto =
            traineeMapper.entityToTrainerTraineeResponseDto(null);

        //then
        assertNull(responseDto);
    }

    @Test
    public void testEntityToTrainerTraineeResponseDto() {
        //given
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        TraineeEntity traineeEntity =
            new TraineeEntity(1L, LocalDate.now(), "myAddress",
                user, new ArrayList<>(), new HashSet<>());

        //when
        TrainerProfileTraineeResponseDto responseDto =
            traineeMapper.entityToTrainerTraineeResponseDto(traineeEntity);

        //then
        assertNotNull(responseDto);
        assertEquals(user.getUsername(), responseDto.getUsername());
    }

    @Test
    public void testUpdateDtoToEntity() {
        //given
        TraineeUpdateRequestDto traineeDto = new TraineeUpdateRequestDto("A.A", "A", "B",
            LocalDate.now(), "myAddress", false);

        //when
        TraineeEntity trainee = traineeMapper.updateDtoToEntity(traineeDto);

        //then
        assertNotNull(trainee);
        assertEquals("A.A", trainee.getUser().getUsername());
        assertEquals("myAddress", trainee.getAddress());
    }


    @Test
    public void testUpdateDtoToEntityNull() {
        //when
        TraineeEntity trainee = traineeMapper.updateDtoToEntity(null);

        //then
        assertNull(trainee);
    }
}
