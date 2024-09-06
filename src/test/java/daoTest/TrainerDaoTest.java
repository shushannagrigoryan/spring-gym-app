package daoTest;

import org.example.dao.TrainerDao;
import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalIdException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerDaoTest {
    @Mock
    private Map<Long, TrainerEntity> trainerStorage;

    @InjectMocks
    private TrainerDao trainerDao;

    @Test
    public void testGetTrainerByIdSuccess(){
        Long id = 1L;
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUserId(id);

        when(trainerStorage.get(id)).thenReturn(trainerEntity);
        Optional<TrainerEntity> actualTrainer = trainerDao.getTrainerById(id);
        assertTrue(actualTrainer.isPresent());
        verify(trainerStorage, times(1)).get(id);
    }

    @Test
    void testGetTrainerByIdFailure() {
        when(trainerStorage.get(1L)).thenReturn(null);

        Optional<TrainerEntity> result = trainerDao.getTrainerById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void generateIdEmptyMap(){
        when(trainerStorage.values()).thenReturn(Collections.emptyList());

        Long generatedId = trainerDao.generateId();
        assertEquals(0L, generatedId);
    }




    @Test
    void testGetTrainerByUsernameNotFound() {
        when(trainerStorage.values()).thenReturn(Collections.emptyList());

        Optional<TrainerEntity> result = trainerDao.getTrainerByUsername("JackSmith");

        assertFalse(result.isPresent());
    }



    @Test
    void testUpdateTrainerById() {
        TrainerEntity trainerEntity = new TrainerEntity();

        when(trainerStorage.containsKey(1L)).thenReturn(true);

        trainerDao.updateTrainerById(1L, trainerEntity);

        verify(trainerStorage).put(1L, trainerEntity);
    }

    @Test
    void testUpdateTrainerByIdThrowsException() {
        TrainerEntity trainerEntity = new TrainerEntity();
        when(trainerStorage.containsKey(1L)).thenReturn(false);

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
        when(trainerStorage.values()).thenReturn(map.values());

        Long generatedId = trainerDao.generateId();

        assertEquals(2L, generatedId);
    }
}
