//package daotest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.lenient;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import org.example.dao.IdGenerator;
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingEntity;
//import org.example.exceptions.GymIllegalEntityTypeException;
//import org.example.storage.DataStorage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class IdGeneratorTest {
//    @Mock
//    private DataStorage dataStorage;
//
//    @Mock
//    private Map<Long, TraineeEntity> traineeEntityMap = new HashMap<>();
//
//    @Mock
//    private Map<Long, TrainerEntity> trainerEntityMap = new HashMap<>();
//
//    @Mock
//    private Map<Long, TrainingEntity> trainingEntityMap = new HashMap<>();
//    @InjectMocks
//    private IdGenerator idGenerator;
//
//    @BeforeEach
//    void setUp() {
//        lenient().when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
//        lenient().when(dataStorage.getTrainerStorage()).thenReturn(trainerEntityMap);
//        lenient().when(dataStorage.getTrainingStorage()).thenReturn(trainingEntityMap);
//    }
//
//    @Test
//    public void testGenerateIdForTraineeEmptyMap() {
//        //given
//        when(traineeEntityMap.keySet()).thenReturn(Collections.emptySet());
//
//        //when
//        Long generatedId = idGenerator.generateId("Trainee");
//
//        //then
//        verify(traineeEntityMap).keySet();
//        assertEquals(0L, generatedId);
//    }
//
//    @Test
//    void testGenerateIdForTraineeWhenStorageHasElements() {
//        //given
//        Set<Long> keySet = new HashSet<>();
//        keySet.add(1L);
//        keySet.add(6L);
//        keySet.add(4L);
//
//        when(traineeEntityMap.keySet())
//                .thenReturn(keySet);
//
//        //when
//        Long generatedId = idGenerator.generateId("Trainee");
//
//        //then
//        assertEquals(7L, generatedId);
//    }
//
//    @Test
//    void testGenerateIdWrongEntityType() {
//        String entityType = "AnotherEntity";
//        GymIllegalEntityTypeException exception =
//                assertThrows(GymIllegalEntityTypeException.class, () -> idGenerator.generateId(entityType));
//        assertEquals("Illegal storage name " + entityType, exception.getMessage());
//    }
//
//    @Test
//    public void testGenerateIdForTrainerEmptyMap() {
//        //given
//        when(trainerEntityMap.keySet()).thenReturn(Collections.emptySet());
//
//        //when
//        Long generatedId = idGenerator.generateId("Trainer");
//
//        //then
//        verify(trainerEntityMap).keySet();
//        assertEquals(0L, generatedId);
//    }
//
//    @Test
//    public void testGenerateIdForTrainingEmptyMap() {
//        //given
//        when(trainingEntityMap.keySet()).thenReturn(Collections.emptySet());
//
//        //when
//        Long generatedId = idGenerator.generateId("Training");
//
//        //then
//        verify(trainingEntityMap).keySet();
//        assertEquals(0L, generatedId);
//    }
//
//}
