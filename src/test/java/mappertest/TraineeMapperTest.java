package mappertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
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
        TraineeEntity traineeEntity =
                new TraineeEntity("Jack", "Smith",
                        "myPassword", LocalDate.now(), "myAddress");

        //when
        TraineeDto traineeDto = traineeMapper.entityToDto(traineeEntity);

        //then
        assertNotNull(traineeDto);
        assertEquals(traineeEntity.getFirstName(), traineeDto.getFirstName());
        assertEquals(traineeEntity.getLastName(), traineeDto.getLastName());
        assertEquals(traineeEntity.getPassword(), traineeDto.getPassword());
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
                        "myPassword", LocalDate.now(), "myAddress");

        //when
        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);

        //then
        assertNotNull(traineeEntity);
        assertEquals(traineeDto.getFirstName(), traineeEntity.getFirstName());
        assertEquals(traineeDto.getLastName(), traineeEntity.getLastName());
        assertEquals(traineeDto.getPassword(), traineeEntity.getPassword());
        assertEquals(traineeDto.getAddress(), traineeEntity.getAddress());
        assertEquals(traineeDto.getDateOfBirth(), traineeEntity.getDateOfBirth());
    }
}
