package daotest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.example.dao.TrainerDao;
import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalIdException;
import org.example.storage.DataStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerDaoTest {
    @Mock
    private DataStorage dataStorage;

    @InjectMocks
    private TrainerDao trainerDao;

    @Test
    public void testGetTrainerByIdSuccess() {
        Long id = 1L;
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUserId(id);

        when(dataStorage.getTrainerStorage().get(id)).thenReturn(trainerEntity);
        Optional<TrainerEntity> actualTrainer = trainerDao.getTrainerById(id);
        assertTrue(actualTrainer.isPresent());
        verify(dataStorage.getTrainerStorage(), times(1)).get(id);
    }

    @Test
    void testGetTrainerByIdFailure() {
        when(dataStorage.getTrainerStorage().get(1L)).thenReturn(null);

        Optional<TrainerEntity> result = trainerDao.getTrainerById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void generateIdEmptyMap() {
        when(dataStorage.getTrainerStorage().values()).thenReturn(Collections.emptyList());

        Long generatedId = trainerDao.generateId();
        assertEquals(0L, generatedId);
    }


    @Test
    void testGetTrainerByUsernameNotFound() {
        when(dataStorage.getTrainerStorage().values()).thenReturn(Collections.emptyList());

        Optional<TrainerEntity> result = trainerDao.getTrainerByUsername("JackSmith");

        assertFalse(result.isPresent());
    }


    @Test
    void testUpdateTrainerById() {
        TrainerEntity trainerEntity = new TrainerEntity();

        when(dataStorage.getTrainerStorage().containsKey(1L)).thenReturn(true);

        trainerDao.updateTrainerById(1L, trainerEntity);

        verify(dataStorage.getTrainerStorage()).put(1L, trainerEntity);
    }

    @Test
    void testUpdateTrainerByIdThrowsException() {
        TrainerEntity trainerEntity = new TrainerEntity();
        when(dataStorage.getTrainerStorage().containsKey(1L)).thenReturn(false);

        assertThatThrownBy(() -> trainerDao.updateTrainerById(1L, trainerEntity))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainer with id: " + 1L);
    }

    @Test
    void testGenerateIdWhenStorageHasElements() {
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUserId(1L);

        Map<Long, TrainerEntity> map = new HashMap<>();
        map.put(1L, trainerEntity);
        when(dataStorage.getTrainerStorage().values()).thenReturn(map.values());

        Long generatedId = trainerDao.generateId();

        assertEquals(2L, generatedId);
    }
}
