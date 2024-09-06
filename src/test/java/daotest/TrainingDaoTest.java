package daotest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.example.dao.TrainingDao;
import org.example.entity.TrainingEntity;
import org.example.storage.DataStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingDaoTest {
    @Mock
    private DataStorage dataStorage;

    @InjectMocks
    private TrainingDao trainingDao;

    @Test
    public void testGetTrainingByIdSuccess() {
        Long id = 1L;
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingId(id);

        when(dataStorage.getTrainingStorage().get(id)).thenReturn(trainingEntity);
        Optional<TrainingEntity> actualTraining = trainingDao.getTrainingById(id);
        assertTrue(actualTraining.isPresent());
        verify(dataStorage.getTrainingStorage(), times(1)).get(id);
    }

    @Test
    void testGetTrainingByIdFailure() {
        when(dataStorage.getTrainingStorage().get(1L)).thenReturn(null);

        Optional<TrainingEntity> result = trainingDao.getTrainingById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void generateIdEmptyMap() {
        when(dataStorage.getTrainingStorage().values()).thenReturn(Collections.emptyList());

        Long generatedId = trainingDao.generateId();
        assertEquals(0L, generatedId);
    }

    @Test
    void testGenerateIdWhenStorageHasElements() {
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingId(1L);

        Map<Long, TrainingEntity> map = new HashMap<>();
        map.put(1L, trainingEntity);
        when(dataStorage.getTrainingStorage().values()).thenReturn(map.values());

        Long generatedId = trainingDao.generateId();

        assertEquals(2L, generatedId);
    }
}
