package daotest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.example.dao.IdGenerator;
import org.example.dao.TrainingDao;
import org.example.entity.TrainingEntity;
import org.example.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingDaoTest {
    @Mock
    private DataStorage dataStorage;

    @Spy
    private Map<Long, TrainingEntity> trainingEntityMap = new HashMap<>();
    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private TrainingDao trainingDao;

    @BeforeEach
    void setUp() {
        lenient().when(dataStorage.getTrainingStorage()).thenReturn(trainingEntityMap);
    }

    @Test
    public void testCreateTraining() {
        //given
        Long id = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        when(idGenerator.generateId("Training")).thenReturn(id);

        //when
        trainingDao.createTraining(trainingEntity);

        //then
        verify(trainingEntityMap, times(1)).put(id, trainingEntity);
        assertTrue(trainingEntityMap.containsKey(id));
    }

    @Test
    public void testGetTrainingByIdSuccess() {
        //given
        Long id = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingId(id);
        when(trainingEntityMap.get(id)).thenReturn(trainingEntity);

        //when
        Optional<TrainingEntity> result = trainingDao.getTrainingById(id);

        //then
        assertTrue(result.isPresent());
        verify(dataStorage.getTrainingStorage(), times(1)).get(id);
    }

    @Test
    void testGetTrainingByIdFailure() {
        //given
        Long id = 1L;
        when(trainingEntityMap.get(id)).thenReturn(null);

        //when
        Optional<TrainingEntity> result = trainingDao.getTrainingById(id);

        //then
        verify(dataStorage.getTrainingStorage(), times(1)).get(id);
        assertFalse(result.isPresent());
    }
}
