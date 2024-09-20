//package daotest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.lenient;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import org.example.dao.UserDao;
//import org.example.dto.TraineeDto;
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainerEntity;
//import org.example.services.TraineeService;
//import org.example.services.TrainerService;
//import org.example.storage.DataStorage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class UserDaoTest {
//    @Mock
//    private TraineeService traineeService;
//
//    @Mock
//    private TrainerService trainerService;
//
//    @Mock
//    private DataStorage dataStorage;
//
//    @Mock
//    private Map<Long, TraineeEntity> traineeEntityMap;
//
//    @Mock
//    private Map<Long, TrainerEntity> trainerEntityMap;
//
//    @InjectMocks
//    private UserDao userDao;
//
//    @BeforeEach
//    void setUp() {
//        lenient().when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
//        lenient().when(dataStorage.getTrainerStorage()).thenReturn(trainerEntityMap);
//    }
//
//    @Test
//    public void testGenerateUsernameWithoutSuffix() {
//        //given
//        String username = "John.Smith";
//        when(traineeService.getTraineeByUsername(username)).thenReturn(null);
//        when(trainerService.getTrainerByUsername(username)).thenReturn(null);
//
//        //when
//        String generatedUsername = userDao.generateUsername("John", "Smith");
//
//        //then
//        verify(traineeService).getTraineeByUsername(username);
//        verify(traineeService).getTraineeByUsername(username);
//        assertEquals(generatedUsername, username);
//
//    }
//
//    @Test
//    public void testGenerateUsernameWithSuffix() {
//        //given
//        String username = "John.Smith";
//
//        when(traineeService.getTraineeByUsername(username)).thenReturn(new TraineeDto());
//        when(trainerService.getTrainerByUsername(username)).thenReturn(null);
//        when(dataStorage.getTraineeStorage()).thenReturn(traineeEntityMap);
//        when(dataStorage.getTrainerStorage()).thenReturn(trainerEntityMap);
//
//        List<TraineeEntity> trainees = getTraineeEntities(username);
//        List<TrainerEntity> trainers = getTrainerEntities(username);
//        when(traineeEntityMap.values()).thenReturn(trainees);
//        when(trainerEntityMap.values()).thenReturn(trainers);
//
//
//        //when
//        String generatedUsername = userDao.generateUsername("John", "Smith");
//
//        String expectedUsername = "John.Smith11";
//
//        //then
//        verify(traineeService).getTraineeByUsername(username);
//        verify(traineeService).getTraineeByUsername(username);
//        assertEquals(generatedUsername, expectedUsername);
//
//    }
//
//    private static List<TraineeEntity> getTraineeEntities(String username) {
//        TraineeEntity traineeEntity1 = new TraineeEntity();
//        traineeEntity1.setUsername(username.concat(String.valueOf(5L)));
//        TraineeEntity traineeEntity2 = new TraineeEntity();
//        traineeEntity2.setUsername(username.concat(String.valueOf(0L)));
//        TraineeEntity traineeEntity3 = new TraineeEntity();
//        traineeEntity3.setUsername(username.concat(String.valueOf(8L)));
//        List<TraineeEntity> trainees = new ArrayList<>();
//
//        trainees.add(traineeEntity1);
//        trainees.add(traineeEntity2);
//        trainees.add(traineeEntity3);
//        return trainees;
//    }
//
//    private static List<TrainerEntity> getTrainerEntities(String username) {
//        TrainerEntity trainerEntity1 = new TrainerEntity();
//        trainerEntity1.setUsername(username.concat(String.valueOf(10L)));
//        TrainerEntity trainerEntity2 = new TrainerEntity();
//        trainerEntity2.setUsername(username.concat(String.valueOf(4L)));
//        List<TrainerEntity> trainers = new ArrayList<>();
//
//        trainers.add(trainerEntity1);
//        trainers.add(trainerEntity2);
//        return trainers;
//    }
//}
