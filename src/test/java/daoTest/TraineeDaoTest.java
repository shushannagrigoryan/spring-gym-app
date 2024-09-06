package daoTest;

import org.example.dao.TraineeDao;
import org.example.entity.TraineeEntity;
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
public class TraineeDaoTest {
    @Mock
    private Map<Long, TraineeEntity> traineeStorage;

    @InjectMocks
    private TraineeDao traineeDao;

    @Test
    public void testGetTraineeByIdSuccess(){
        Long id = 1L;
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUserId(id);

        when(traineeStorage.get(id)).thenReturn(traineeEntity);
        Optional<TraineeEntity> actualTrainee = traineeDao.getTraineeById(id);
        assertTrue(actualTrainee.isPresent());
        verify(traineeStorage, times(1)).get(id);
    }

    @Test
    void testGetTraineeByIdFailure() {
        when(traineeStorage.get(1L)).thenReturn(null);

        Optional<TraineeEntity> result = traineeDao.getTraineeById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void generateIdEmptyMap(){
        when(traineeStorage.values()).thenReturn(Collections.emptyList());

        Long generatedId = traineeDao.generateId();
        assertEquals(0L, generatedId);
    }

    @Test
    void testDeleteTraineeById() {
        when(traineeStorage.containsKey(1L)).thenReturn(true);

        traineeDao.deleteTraineeById(1L);

        verify(traineeStorage).remove(1L);
    }

    @Test
    void testDeleteTraineeByIdThrowsException() {
        when(traineeStorage.containsKey(1L)).thenReturn(false);
        assertThatThrownBy(() -> traineeDao.deleteTraineeById(1L))
                .isInstanceOf(IllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + 1L);
    }


    @Test
    void testGetTraineeByUsernameNotFound() {
        when(traineeStorage.values()).thenReturn(Collections.emptyList());

        Optional<TraineeEntity> result = traineeDao.getTraineeByUsername("JackSmith");

        assertFalse(result.isPresent());
    }



    @Test
    void testUpdateTraineeById() {
        TraineeEntity traineeEntity = new TraineeEntity();

        when(traineeStorage.containsKey(1L)).thenReturn(true);

        traineeDao.updateTraineeById(1L, traineeEntity);

        verify(traineeStorage).put(1L, traineeEntity);
    }

    @Test
    void testUpdateTraineeByIdThrowsException() {
        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeStorage.containsKey(1L)).thenReturn(false);

        assertThatThrownBy(() -> traineeDao.updateTraineeById(1L, traineeEntity))
                .isInstanceOf(IllegalIdException.class)
                        .hasMessageContaining("No trainee with id: " + 1L);
    }

    @Test
    void testGenerateIdWhenStorageHasElements() {
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUserId(1L);

        Map<Long, TraineeEntity> map = new HashMap<>();
        map.put(1L, traineeEntity);
        when(traineeStorage.values()).thenReturn(map.values());

        Long generatedId = traineeDao.generateId();

        assertEquals(2L, generatedId);
    }
}
