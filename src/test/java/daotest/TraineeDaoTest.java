package daotest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.example.dao.IdGenerator;
import org.example.dao.TraineeDao;
import org.example.entity.TraineeEntity;
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
public class TraineeDaoTest {
    @Mock
    private DataStorage dataStorage;

    @Spy
    private Map<Long, TraineeEntity> traineeEntityMap = new HashMap<>();
    @Spy
    private Map<String, TraineeEntity> traineeEntityMapUsernameKey = new HashMap<>();
    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private TraineeDao traineeDao;

    @BeforeEach
    void setUp() {
        lenient().when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
        lenient().when(dataStorage.getTraineeStorageUsernameKey()).thenReturn(traineeEntityMapUsernameKey);
    }

    @Test
    public void testCreateTrainee() {
        //given
        Long id = 1L;
        String username = "A.B";
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUsername(username);
        when(idGenerator.generateId("Trainee")).thenReturn(id);

        //when
        traineeDao.createTrainee(traineeEntity);

        //then
        verify(traineeEntityMap, times(1)).put(id, traineeEntity);
        verify(traineeEntityMapUsernameKey, times(1)).put(username, traineeEntity);
        assertTrue(traineeEntityMap.containsKey(id));
        assertTrue(traineeEntityMapUsernameKey.containsKey(username));
    }

    @Test
    public void testGetTraineeByIdSuccess() {
        //given
        Long id = 1L;
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUserId(id);
        when(traineeEntityMap.get(id)).thenReturn(traineeEntity);

        //when
        Optional<TraineeEntity> result = traineeDao.getTraineeById(id);

        //then
        assertTrue(result.isPresent());
        verify(dataStorage.getTraineeStorage(), times(1)).get(id);
    }

    @Test
    void testGetTraineeByIdFailure() {
        //given
        Long id = 1L;
        when(traineeEntityMap.get(id)).thenReturn(null);

        //when
        Optional<TraineeEntity> result = traineeDao.getTraineeById(id);

        //then
        verify(dataStorage.getTraineeStorage(), times(1)).get(id);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteTraineeByIdSuccess() {
        //given
        when(traineeEntityMap.containsKey(1L)).thenReturn(true);

        //when
        traineeDao.deleteTraineeById(1L);

        //then
        verify(dataStorage.getTraineeStorage(), times(1)).remove(1L);
    }

    @Test
    void testDeleteTraineeByIdThrowsException() {
        //given
        when(traineeEntityMap.containsKey(1L)).thenReturn(false);

        //then
        assertThatThrownBy(() -> traineeDao.deleteTraineeById(1L))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + 1L);
    }

    @Test
    void testUpdateTraineeByIdSuccess() {
        //given
        Long id = 1L;
        TraineeEntity traineeEntity = new TraineeEntity("A", "B",
                "myPassword", LocalDate.now(), "myAddress");
        traineeEntity.setUserId(id);

        when(traineeEntityMap.containsKey(id)).thenReturn(true);

        //when
        traineeDao.updateTraineeById(id, traineeEntity);

        //then
        verify(traineeEntityMap).put(id, traineeEntity);
        verify(traineeEntityMapUsernameKey).put(traineeEntity.getUsername(), traineeEntity);
        assertTrue(traineeEntityMap.containsKey(id));
        assertTrue(traineeEntityMapUsernameKey.containsKey(traineeEntity.getUsername()));
    }

    @Test
    void testUpdateTraineeByIdThrowsException() {
        //given
        Long id = 1L;
        TraineeEntity traineeEntity = new TraineeEntity("A", "B",
                "myPassword", LocalDate.now(), "myAddress");
        traineeEntity.setUserId(id);
        when(traineeEntityMap.containsKey(id)).thenReturn(false);

        //then
        assertThatThrownBy(() -> traineeDao.updateTraineeById(id, traineeEntity))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + id);
    }



    @Test
    void testGetTraineeByUsernameNotFound() {
        //given
        String username = "Jack.Smith";
        when(traineeEntityMapUsernameKey.get(username)).thenReturn(null);

        //when
        Optional<TraineeEntity> result = traineeDao.getTraineeByUsername(username);

        //then
        assertFalse(result.isPresent());
    }

    @Test
    void testGetTraineeByUsernameFound() {
        //given
        String username = "Jack.Smith";
        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeEntityMapUsernameKey.get(username)).thenReturn(traineeEntity);

        //when
        Optional<TraineeEntity> result = traineeDao.getTraineeByUsername(username);

        //then
        assertTrue(result.isPresent());
    }

}
