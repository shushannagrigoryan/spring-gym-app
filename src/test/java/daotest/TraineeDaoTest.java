package daotest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.example.dao.TraineeDao;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.storage.DataStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeDaoTest {
    @Mock
    private DataStorage dataStorage;

    @Mock
    private Map<Long, TraineeEntity> traineeEntityMap;

    @InjectMocks
    private TraineeDao traineeDao;

    @Test
    public void testGetTraineeByIdSuccess() {
        Long id = 1L;
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUserId(id);
        when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
        when(traineeEntityMap.get(id)).thenReturn(traineeEntity);

        Optional<TraineeEntity> actualTrainee = traineeDao.getTraineeById(id);

        assertTrue(actualTrainee.isPresent());
        verify(dataStorage.getTraineeStorage(), times(1)).get(id);
    }

    @Test
    void testGetTraineeByIdFailure() {
        when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
        when(traineeEntityMap.get(1L)).thenReturn(null);

        Optional<TraineeEntity> result = traineeDao.getTraineeById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void generateIdEmptyMap() {
        when(dataStorage.getTraineeStorage()).thenReturn(Collections.emptyMap());

        Long generatedId = traineeDao.generateId();
        assertEquals(0L, generatedId);
    }

    @Test
    void testDeleteTraineeById() {

        when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
        when(traineeEntityMap.containsKey(1L)).thenReturn(true);

        traineeDao.deleteTraineeById(1L);

        verify(dataStorage.getTraineeStorage()).remove(1L);
    }

    @Test
    void testDeleteTraineeByIdThrowsException() {
        when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
        when(traineeEntityMap.containsKey(1L)).thenReturn(false);
        assertThatThrownBy(() -> traineeDao.deleteTraineeById(1L))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + 1L);
    }

    /*

   @Test
    void testGetTraineeByUsernameNotFound() {
        when(dataStorage.getTraineeStorage().values()).thenReturn(Collections.emptyList());

        Optional<TraineeEntity> result = traineeDao.getTraineeByUsername("JackSmith");

        assertFalse(result.isPresent());
    }

     */


    @Test
    void testUpdateTraineeById() {
        TraineeEntity traineeEntity = new TraineeEntity();
        when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
        when(traineeEntityMap.containsKey(1L)).thenReturn(true);

        traineeDao.updateTraineeById(1L, traineeEntity);

        verify(dataStorage.getTraineeStorage()).put(1L, traineeEntity);
    }

    @Test
    void testUpdateTraineeByIdThrowsException() {
        TraineeEntity traineeEntity = new TraineeEntity();
        when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
        when(traineeEntityMap.containsKey(1L)).thenReturn(false);

        assertThatThrownBy(() -> traineeDao.updateTraineeById(1L, traineeEntity))
                .isInstanceOf(GymIllegalIdException.class)
                .hasMessageContaining("No trainee with id: " + 1L);
    }

    //    @Test
    //    void testGenerateIdWhenStorageHasElements() {
    //        TraineeEntity traineeEntity = new TraineeEntity();
    //        traineeEntity.setUserId(1L);
    //
    //        Map<Long, TraineeEntity> map = new HashMap<>();
    //        map.put(1L, traineeEntity);
    //        when(dataStorage.getTraineeStorage().values()).thenReturn(map.values());
    //
    //        Long generatedId = traineeDao.generateId();
    //
    //        assertEquals(2L, generatedId);
    //    }
}
