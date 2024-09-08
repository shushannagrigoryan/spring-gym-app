package daotest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.example.dao.TrainerDao;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerDaoTest {
    @Mock
    private DataStorage dataStorage;

    @Spy
    private Map<Long, TrainerEntity> trainerEntityMap = new HashMap<>();
    @Spy
    private Map<String, TrainerEntity> trainerEntityMapUsernameKey = new HashMap<>();
    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private TrainerDao trainerDao;

    @BeforeEach
    void setUp() {
        lenient().when(dataStorage.getTrainerStorage()).thenReturn(trainerEntityMap);
        lenient().when(dataStorage.getTrainerStorageUsernameKey()).thenReturn(trainerEntityMapUsernameKey);
    }

    @Test
    public void testCreateTrainer() {
        //given
        Long id = 1L;
        String username = "A.B";
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUsername(username);
        when(idGenerator.generateId("Trainer")).thenReturn(id);

        //when
        trainerDao.createTrainer(trainerEntity);

        //then
        verify(trainerEntityMap, times(1)).put(id, trainerEntity);
        verify(trainerEntityMapUsernameKey, times(1)).put(username, trainerEntity);
        assertTrue(trainerEntityMap.containsKey(id));
        assertTrue(trainerEntityMapUsernameKey.containsKey(username));
    }

    @Test
    void testGetTrainerByIdFailure() {
        //given
        Long id = 1L;
        when(trainerEntityMap.get(id)).thenReturn(null);

        //when
        Optional<TrainerEntity> result = trainerDao.getTrainerById(id);

        //then
        verify(dataStorage.getTrainerStorage(), times(1)).get(id);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetTrainerByIdSuccess() {
        //given
        Long id = 1L;
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUserId(id);
        when(trainerEntityMap.get(id)).thenReturn(trainerEntity);

        //when
        Optional<TrainerEntity> result = trainerDao.getTrainerById(id);

        //then
        assertTrue(result.isPresent());
        verify(dataStorage.getTrainerStorage(), times(1)).get(id);
    }

    @Test
    void testUpdateTrainerByIdSuccess() {
        //given
        Long id = 1L;
        TrainerEntity trainerEntity = new TrainerEntity("A", "B",
                "myPassword", "boxing");
        trainerEntity.setUserId(id);

        when(trainerEntityMap.containsKey(id)).thenReturn(true);

        //when
        trainerDao.updateTrainerById(id, trainerEntity);

        //then
        verify(trainerEntityMap).put(id, trainerEntity);
        verify(trainerEntityMapUsernameKey).put(trainerEntity.getUsername(), trainerEntity);
        assertTrue(trainerEntityMap.containsKey(id));
        assertTrue(trainerEntityMapUsernameKey.containsKey(trainerEntity.getUsername()));
    }

    @Test
    void testUpdateTrainerByIdThrowsException() {
        //given
        Long id = 1L;
        TrainerEntity trainerEntity = new TrainerEntity("A", "B",
                "myPassword", "boxing");
        trainerEntity.setUserId(id);
        when(trainerEntityMap.containsKey(id)).thenReturn(false);

        //then
        assertThatThrownBy(() -> trainerDao.updateTrainerById(id, trainerEntity))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining("No trainer with id: " + id);
    }



    @Test
    void testGetTrainerByUsernameNotFound() {
        //given
        String username = "Jack.Smith";
        when(trainerEntityMapUsernameKey.get(username)).thenReturn(null);

        //when
        Optional<TrainerEntity> result = trainerDao.getTrainerByUsername(username);

        //then
        assertFalse(result.isPresent());
    }

    @Test
    void testGetTrainerByUsernameFound() {
        //given
        String username = "Jack.Smith";
        TrainerEntity trainerEntity = new TrainerEntity();
        when(trainerEntityMapUsernameKey.get(username)).thenReturn(trainerEntity);

        //when
        Optional<TrainerEntity> result = trainerDao.getTrainerByUsername(username);

        //then
        assertTrue(result.isPresent());
    }



}
