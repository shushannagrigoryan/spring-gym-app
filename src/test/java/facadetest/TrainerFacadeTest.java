//package facadetest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import org.example.auth.TrainerAuth;
//import org.example.dto.TrainerDto;
//import org.example.entity.TrainerEntity;
//import org.example.facade.TrainerFacade;
//import org.example.mapper.TrainerMapper;
//import org.example.services.TrainerService;
//import org.example.validation.TrainerValidation;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerFacadeTest {
//    @Mock
//    private TrainerService trainerService;
//    @Mock
//    private TrainerMapper trainerMapper;
//    @Mock
//    private TrainerValidation trainerValidation;
//    @Mock
//    private TrainerAuth trainerAuth;
//    @InjectMocks
//    private TrainerFacade trainerFacade;
//
//
//    @Test
//    public void testCreateTrainerSuccess() {
//        //given
//        TrainerDto trainerDto = new TrainerDto();
//        TrainerEntity trainerEntity = new TrainerEntity();
//        doNothing().when(trainerValidation).validateTrainer(trainerDto);
//        when(trainerMapper.dtoToEntity(trainerDto)).thenReturn(trainerEntity);
//        doNothing().when(trainerService).createTrainer(trainerEntity);
//
//        //when
//        trainerFacade.createTrainer(trainerDto);
//
//        //then
//        verify(trainerService).createTrainer(trainerEntity);
//    }
//
//    @Test
//    public void testGetTrainerByIdSuccess() {
//        //given
//        Long id = 1L;
//        TrainerDto trainerDto = new TrainerDto();
//        TrainerEntity trainerEntity = new TrainerEntity();
//        when(trainerService.getTrainerById(id)).thenReturn(trainerEntity);
//        when(trainerMapper.entityToDto(trainerEntity)).thenReturn(trainerDto);
//
//        //when
//        TrainerDto result = trainerFacade.getTrainerById(id);
//
//        //then
//        verify(trainerService).getTrainerById(id);
//        assertNotNull(result);
//    }
//
//    @Test
//    public void testGetTrainerByUsernameSuccess() {
//        //given
//        String username = "1234567890";
//        TrainerDto trainerDto = new TrainerDto();
//        TrainerEntity trainerEntity = new TrainerEntity();
//        when(trainerService.getTrainerByUsername(username)).thenReturn(trainerEntity);
//        when(trainerMapper.entityToDto(trainerEntity)).thenReturn(trainerDto);
//
//        //when
//        TrainerDto result = trainerFacade.getTrainerByUsername(username);
//
//        //then
//        verify(trainerService).getTrainerByUsername(username);
//        assertNotNull(result);
//    }
//
//    @Test
//    public void testChangeTrainerPasswordSuccess() {
//        //given
//        String username = "A.B";
//        String password = "1234567890";
//        when(trainerAuth.trainerAuth(username, password)).thenReturn(true);
//
//        //when
//        trainerFacade.changeTrainerPassword(username, password);
//
//        //then
//        verify(trainerService).changeTrainerPassword(username);
//    }
//
//    @Test
//    public void testChangeTrainerPasswordFailure() {
//        //given
//        String username = "A.B";
//        String password = "1234567890";
//        when(trainerAuth.trainerAuth(username, password)).thenReturn(false);
//
//        //when
//        trainerFacade.changeTrainerPassword(username, password);
//
//        //then
//        verify(trainerService, times(0)).changeTrainerPassword(username);
//    }
//
//    @Test
//    public void testUpdateTrainerByIdIllegalId() {
//        //given
//        Long id = 1L;
//        TrainerDto trainerDto = new TrainerDto();
//        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
//        doThrow(new RuntimeException("No trainer with id: " + id))
//                .when(trainerValidation).validateTrainer(trainerDto);
//
//        //then
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> trainerFacade.updateTrainerById(id, trainerDto));
//        assertEquals("No trainer with id: " + id, exception.getMessage());
//        verify(trainerService, times(0))
//                .updateTrainerById(id, trainerEntity);
//    }
//
//    @Test
//    public void testActivateTrainer() {
//        //given
//        Long id = 1L;
//        doNothing().when(trainerService).activateTrainer(id);
//
//        //when
//        trainerFacade.activateTrainer(id);
//
//        //then
//        verify(trainerService).activateTrainer(id);
//    }
//
//    @Test
//    public void testDeactivateTrainer() {
//        //given
//        Long id = 1L;
//        doNothing().when(trainerService).deactivateTrainer(id);
//
//        //when
//        trainerFacade.deactivateTrainer(id);
//
//        //then
//        verify(trainerService).deactivateTrainer(id);
//    }
//
//
//    @Test
//    public void testUpdateTrainerByIdSuccess() {
//        //given
//        Long id = 1L;
//        TrainerDto trainerDto = new TrainerDto();
//        TrainerEntity trainerEntity = new TrainerEntity();
//        doNothing().when(trainerValidation).validateTrainer(trainerDto);
//        when(trainerMapper.dtoToEntity(trainerDto)).thenReturn(trainerEntity);
//        doNothing().when(trainerService).updateTrainerById(id, trainerEntity);
//
//        //when
//        trainerFacade.updateTrainerById(id, trainerDto);
//
//        //then
//        verify(trainerService).updateTrainerById(id, trainerEntity);
//    }
//
//
//    @Test
//    public void testGetTrainersNotAssignedToTrainee_WhenTrainersFound() {
//        // Given
//        String traineeUsername = "A.A";
//        TrainerEntity trainer1 = new TrainerEntity();
//        TrainerEntity trainer2 = new TrainerEntity();
//        TrainerDto trainerDto1 = new TrainerDto();
//        TrainerDto trainerDto2 = new TrainerDto();
//
//        when(trainerService.getTrainersNotAssignedToTrainee(traineeUsername))
//                .thenReturn(Arrays.asList(trainer1, trainer2));
//        when(trainerMapper.entityToDto(trainer1)).thenReturn(trainerDto1);
//        when(trainerMapper.entityToDto(trainer2)).thenReturn(trainerDto2);
//
//        // When
//        List<TrainerDto> result = trainerFacade.getTrainersNotAssignedToTrainee(traineeUsername);
//
//        // Then
//        assertEquals(2, result.size());
//        assertEquals(trainerDto1, result.get(0));
//        assertEquals(trainerDto2, result.get(1));
//    }
//
//    @Test
//    public void testGetTrainersNotAssignedToTrainee_WhenNoTrainersFound() {
//        // Given
//        String traineeUsername = "A.A";
//
//        when(trainerService.getTrainersNotAssignedToTrainee(traineeUsername))
//                .thenReturn(Collections.emptyList());
//
//        // When
//        List<TrainerDto> result = trainerFacade.getTrainersNotAssignedToTrainee(traineeUsername);
//
//        // Then
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    public void testGetTrainersNotAssignedToTrainee_WhenServiceThrowsException() {
//        // Given
//        String traineeUsername = "A.A";
//        when(trainerService.getTrainersNotAssignedToTrainee(traineeUsername))
//                .thenThrow(new RuntimeException("Error"));
//
//        // Then
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> trainerFacade.getTrainersNotAssignedToTrainee(traineeUsername));
//        assertEquals("Error", exception.getMessage());
//    }
//}
//
