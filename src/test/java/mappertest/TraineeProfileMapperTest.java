package mappertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import org.example.dto.responsedto.TraineeProfileResponseDto;
import org.example.dto.responsedto.TraineeUpdateResponseDto;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.example.mapper.TraineeProfileMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeProfileMapperTest {
    @InjectMocks
    private TraineeProfileMapper traineeProfileMapper;

    @Test
    public void testEntityToDtoNull() {
        assertNull(traineeProfileMapper.entityToProfileDto(null));
    }

    @Test
    public void testEntityToProfileDto() {
        //given
        UserEntity user = new UserEntity("A", "B", "A.B", "password12");
        TraineeEntity traineeEntity = new TraineeEntity(1L, LocalDate.now(),
                "myAddress", user, new ArrayList<>(), new HashSet<>());


        //when
        TraineeProfileResponseDto traineeDto = traineeProfileMapper.entityToProfileDto(traineeEntity);

        //then
        assertNotNull(traineeDto);
        assertEquals(traineeEntity.getUser().getFirstName(), traineeDto.getFirstName());
        assertEquals(traineeEntity.getUser().getLastName(), traineeDto.getLastName());
        assertEquals(traineeEntity.getAddress(), traineeDto.getAddress());
        assertEquals(traineeEntity.getDateOfBirth(), traineeDto.getDateOfBirth());
    }

    @Test
    public void testDtoToEntityNull() {
        assertNull(traineeProfileMapper.entityToUpdatedDto(null));
    }


    @Test
    public void testEntityToUpdatedDto() {
        //given
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        TraineeEntity traineeEntity =
                new TraineeEntity(1L, LocalDate.now(), "myAddress",
                        user, new ArrayList<>(), new HashSet<>());

        //when
        TraineeUpdateResponseDto responseDto = traineeProfileMapper.entityToUpdatedDto(traineeEntity);

        //then
        assertNotNull(responseDto);
        assertNotNull(responseDto.getUsername());
    }

}
