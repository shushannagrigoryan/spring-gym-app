package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.controller.TrainerController;
import org.example.dto.requestdto.TrainerCreateRequestDto;
import org.example.dto.requestdto.TrainerUpdateRequestDto;
import org.example.dto.requestdto.UserChangeActiveStatusRequestDto;
import org.example.dto.responsedto.TrainerProfileResponseDto;
import org.example.dto.responsedto.TrainerResponseDto;
import org.example.dto.responsedto.TrainerUpdateResponseDto;
import org.example.entity.TrainerEntity;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainerProfileMapper;
import org.example.services.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private TrainerProfileMapper trainerProfileMapper;
    @InjectMocks
    private TrainerController trainerController;


    @Test
    public void testRegisterTrainerSuccess() {
        //given
        TrainerCreateRequestDto requestDto = new TrainerCreateRequestDto();
        TrainerEntity trainerEntity = new TrainerEntity();
        when(trainerMapper.dtoToEntity(requestDto)).thenReturn(trainerEntity);
        TrainerEntity registered = new TrainerEntity();
        when(trainerService.registerTrainer(trainerEntity)).thenReturn(registered);
        TrainerResponseDto response = new TrainerResponseDto();
        when(trainerMapper.entityToResponseDto(registered)).thenReturn(response);

        //when
        ResponseEntity<TrainerResponseDto> result = trainerController.registerTrainer(requestDto);

        //then
        verify(trainerService).registerTrainer(trainerEntity);
        verify(trainerMapper).entityToResponseDto(registered);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

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
    @Test
    public void testChangeActiveStatusSuccess() {
        //given
        String username = "A.B";
        boolean isActive = true;
        UserChangeActiveStatusRequestDto requestDto =
                new UserChangeActiveStatusRequestDto(username, isActive);
        when(trainerService.changeActiveStatus(username, isActive)).thenReturn("Success.");

        //when
        ResponseEntity<String> response = trainerController.changeActiveStatus(requestDto);

        //then
        verify(trainerService).changeActiveStatus(username, isActive);
        assertEquals("Success.", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void getTrainerProfile() {
        //given
        String username = "A.A";
        TrainerProfileResponseDto response = new TrainerProfileResponseDto();
        TrainerEntity trainer = new TrainerEntity();
        when(trainerService.getTrainerProfile(username)).thenReturn(trainer);
        when(trainerProfileMapper.entityToProfileDto(trainer)).thenReturn(response);

        //when
        ResponseEntity<TrainerProfileResponseDto> result =
                trainerController.getTrainerProfile(username);

        //then
        verify(trainerService).getTrainerProfile(username);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testUpdateTrainer() {
        //given
        String username = "A.A";
        String firstName = "B";
        String lastName = "C";
        Boolean isActive = false;
        Long specialization = 1L;
        TrainerUpdateRequestDto requestDto =
                new TrainerUpdateRequestDto(username, firstName, lastName, specialization, isActive);
        TrainerEntity trainer = new TrainerEntity();
        when(trainerMapper.updateDtoToEntity(requestDto)).thenReturn(trainer);
        TrainerEntity updatedTrainer = new TrainerEntity();
        when(trainerService.updateTrainer(trainer)).thenReturn(updatedTrainer);
        TrainerUpdateResponseDto trainerResponse = new TrainerUpdateResponseDto();
        when(trainerProfileMapper
                .entityToUpdatedDto(updatedTrainer)).thenReturn(trainerResponse);

        //when
        ResponseEntity<TrainerUpdateResponseDto> result = trainerController.updateTrainer(requestDto);

        //then
        verify(trainerMapper).updateDtoToEntity(requestDto);
        verify(trainerService).updateTrainer(trainer);
        verify(trainerProfileMapper).entityToUpdatedDto(updatedTrainer);
        assertEquals(trainerResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}

