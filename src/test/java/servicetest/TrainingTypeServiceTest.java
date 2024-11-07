package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.repositories.TrainingTypeRepository;
import org.example.services.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {
    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    public void testGetTrainingTypeByIdSuccess() {
        //given
        Long id = 1L;
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setTrainingTypeName("training type name");
        when(trainingTypeRepository.findById(id)).thenReturn(Optional.of(trainingTypeEntity));
        //when
        TrainingTypeEntity trainingType = trainingTypeService.getTrainingTypeById(id);

        //then
        verify(trainingTypeRepository).findById(id);
        assertEquals(trainingTypeEntity, trainingType);

    }

    @Test
    public void testGetTrainingTypeByIdFailure() {
        //given
        Long id = 1L;
        when(trainingTypeRepository.findById(id)).thenReturn(Optional.empty());

        //then
        assertThrows(GymIllegalIdException.class,
            () -> trainingTypeService.getTrainingTypeById(id),
            String.format("No training type with id: %d", id));
    }

    @Test
    public void testGetAllTrainingTypesSuccess() {
        //given
        List<TrainingTypeEntity> trainingTypes = new ArrayList<>();
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        //when
        List<TrainingTypeEntity> trainingType = trainingTypeService.getAllTrainingTypes();

        //then
        verify(trainingTypeRepository).findAll();
        assertEquals(trainingTypes, trainingType);

    }


}
