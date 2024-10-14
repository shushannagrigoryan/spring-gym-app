package facadetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.example.controller.TrainerController;
import org.example.dto.requestdto.TrainerCreateRequestDto;
import org.example.dto.requestdto.TrainerUpdateRequestDto;
import org.example.dto.requestdto.UserChangeActiveStatusRequestDto;
import org.example.dto.responsedto.TrainerProfileDto;
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



    @Test
    public void testGetActiveTrainersNotAssignedToTrainee() {
        // Given
        String traineeUsername = "A.A";
        TrainerEntity trainer = new TrainerEntity();
        when(trainerService.notAssignedOnTraineeActiveTrainers(traineeUsername)).thenReturn(List.of(trainer));
        TrainerProfileDto trainerProfileDto = new TrainerProfileDto();
        when(trainerMapper.entityToProfileDto(trainer)).thenReturn(trainerProfileDto);

        // When
        ResponseEntity<Set<TrainerProfileDto>> result =
                trainerController.notAssignedOnTraineeActiveTrainers(traineeUsername);

        // Then
        assertEquals(Set.of(trainerProfileDto), result.getBody());
        verify(trainerService).notAssignedOnTraineeActiveTrainers(traineeUsername);
        verify(trainerMapper).entityToProfileDto(trainer);
    }

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

