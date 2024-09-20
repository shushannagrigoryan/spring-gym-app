//package servicetest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//import org.example.dao.TrainingDao;
//import org.example.dto.TraineeDto;
//import org.example.dto.TrainerDto;
//import org.example.dto.TrainingDto;
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainingEntity;
//import org.example.exceptions.GymIllegalIdException;
//import org.example.mapper.TrainingMapper;
//import org.example.services.TraineeService;
//import org.example.services.TrainerService;
//import org.example.services.TrainingService;
//import org.example.storage.SaveDataToFile;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//@ExtendWith(MockitoExtension.class)
//public class TrainingServiceTest {
//    @Mock
//    private TraineeService traineeService;
//    @Mock
//    private TrainerService trainerService;
//    @Mock
//    private TrainingDao trainingDao;
//
//    @Mock
//    private TrainingMapper trainingMapper;
//
//    @Mock
//    private SaveDataToFile saveDataToFile;
//
//    @InjectMocks
//    private TrainingService trainingService;
//
//    @Test
//    public void testCreateTrainingInvalidTraineeId() {
//        //given
//        Long traineeId = 1L;
//        TrainingEntity trainingEntity = new TrainingEntity();
//        trainingEntity.setTraineeId(traineeId);
//        when(traineeService.getTraineeById(traineeId)).thenReturn(null);
//
//        //then
//        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
//                () -> trainingService.createTraining(trainingEntity));
//        assertEquals("No trainee with id: " + traineeId, exception.getMessage());
//        verify(traineeService).getTraineeById(traineeId);
//    }
//
//    @Test
//    public void testCreateTrainingInvalidTrainerId() {
//        //given
//        Long trainerId = 1L;
//        TraineeEntity traineeEntity = new TraineeEntity();
//        Long traineeId = 1L;
//        traineeEntity.setUserId(traineeId);
//        TrainingEntity trainingEntity = new TrainingEntity();
//        trainingEntity.setTrainerId(trainerId);
//        trainingEntity.setTraineeId(traineeId);
//        when(traineeService.getTraineeById(traineeEntity.getUserId())).thenReturn(new TraineeDto());
//        when(trainerService.getTrainerById(trainerId)).thenReturn(null);
//
//        //then
//        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
//                () -> trainingService.createTraining(trainingEntity));
//        assertEquals("No trainer with id: " + trainerId, exception.getMessage());
//        verify(traineeService).getTraineeById(traineeId);
//        verify(trainerService).getTrainerById(trainerId);
//    }
//
//    @Test
//    public void testCreateTrainingSuccess() {
//        //given
//        long traineeId = 1L;
//        long trainerId = 1L;
//        TrainingEntity trainingEntity = new TrainingEntity();
//        trainingEntity.setTraineeId(traineeId);
//        trainingEntity.setTrainerId(trainerId);
//        TraineeDto traineeDto = new TraineeDto();
//        TrainerDto trainerDto = new TrainerDto();
//
//        when(traineeService.getTraineeById(traineeId)).thenReturn(traineeDto);
//        when(trainerService.getTrainerById(trainerId)).thenReturn(trainerDto);
//        doNothing().when(trainingDao).createTraining(trainingEntity);
//        doNothing().when(saveDataToFile).writeMapToFile("Training");
//
//        //when
//        trainingService.createTraining(trainingEntity);
//
//        //then
//        verify(trainingDao).createTraining(trainingEntity);
//        verify(saveDataToFile).writeMapToFile("Training");
//        verify(trainingDao).createTraining(trainingEntity);
//        verify(saveDataToFile).writeMapToFile("Training");
//    }
//
//    @Test
//    public void testGetTrainingByIdSuccess() {
//        //given
//        Long trainingId = 1L;
//        TrainingEntity trainingEntity = new TrainingEntity();
//        trainingEntity.setTrainingId(trainingId);
//
//        when(trainingDao.getTrainingById(trainingId)).thenReturn(Optional.of(trainingEntity));
//        when(trainingMapper.entityToDto(trainingEntity)).thenReturn(new TrainingDto());
//
//        //when
//        trainingService.getTrainingById(trainingId);
//
//        //then
//        verify(trainingDao).getTrainingById(trainingId);
//    }
//
//    @Test
//    public void testGetTrainingByIdInvalidId() {
//        //given
//        Long trainingId = 1L;
//        when(trainingDao.getTrainingById(trainingId)).thenReturn(Optional.empty());
//
//        //then
//        GymIllegalIdException exception = assertThrows(GymIllegalIdException.class,
//                () -> trainingService.getTrainingById(trainingId));
//        assertEquals("No training with id: " + trainingId, exception.getMessage());
//
//        verify(trainingDao).getTrainingById(trainingId);
//    }
//
//
//}
