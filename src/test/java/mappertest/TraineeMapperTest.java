package mappertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import org.example.dto.TraineeDto;
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
        TraineeEntity traineeEntity = new TraineeEntity(1L, LocalDate.now(), "myAddress", user, new ArrayList<>());


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
    public void testDtoToEntityNull() {
        assertNull(traineeMapper.dtoToEntity(null));
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
}
