package mappertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.example.dto.TrainingTypeDto;
import org.example.dto.responsedto.TrainingTypeResponseDto;
import org.example.entity.TrainingTypeEntity;
import org.example.mapper.TrainingTypeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeMapperTest {
    @InjectMocks
    private TrainingTypeMapper trainingTypeMapper;

    @Test
    public void testEntityToDtoNull() {
        assertNull(trainingTypeMapper.entityToDto(null));
    }

    @Test
    public void testEntityToDto() {
        //given
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(1L);
        trainingTypeEntity.setTrainingTypeName("training type");

        //when
        TrainingTypeDto trainingTypeDto = trainingTypeMapper.entityToDto(trainingTypeEntity);

        //then
        assertNotNull(trainingTypeDto);
        assertEquals(trainingTypeEntity.getTrainingTypeName(), trainingTypeDto.getName());
    }

    @Test
    public void testDtoToEntityNull() {
        assertNull(trainingTypeMapper.dtoToEntity(null));
    }

    @Test
    public void testDtoToEntity() {
        //given
        TrainingTypeDto trainingTypeDto =
                new TrainingTypeDto("boxing");

        //when
        TrainingTypeEntity trainingEntity = trainingTypeMapper.dtoToEntity(trainingTypeDto);

        //then
        assertNotNull(trainingEntity);
        assertEquals(trainingEntity.getTrainingTypeName(), trainingTypeDto.getName());
    }

    @Test
    public void testEntityToResponseDto() {
        //given
        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setTrainingTypeName("name");
        trainingType.setId(1L);

        //when
        TrainingTypeResponseDto  typeResponseDto = trainingTypeMapper.entityToResponseDto(trainingType);

        //then
        assertNotNull(typeResponseDto);
        assertEquals("name", typeResponseDto.getTrainingTypeName());
        assertEquals(1L, typeResponseDto.getId());
    }

    @Test
    public void testNullEntityToResponseDto() {
        assertNull(trainingTypeMapper.entityToResponseDto(null));
    }
}
