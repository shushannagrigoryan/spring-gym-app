package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TrainerCreateRequestDto;
import org.example.dto.requestdto.TrainerUpdateRequestDto;
import org.example.dto.requestdto.UserChangeActiveStatusRequestDto;
import org.example.dto.responsedto.TrainerProfileDto;
import org.example.dto.responsedto.TrainerProfileResponseDto;
import org.example.dto.responsedto.TrainerResponseDto;
import org.example.dto.responsedto.TrainerUpdateResponseDto;
import org.example.entity.TrainerEntity;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainerProfileMapper;
import org.example.services.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainers")
@Slf4j
@Tag(name = "TrainerController")
//@Api(value = "TrainerController")
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final TrainerProfileMapper trainerProfileMapper;

    /**
     * Setting dependencies.
     */
    public TrainerController(TrainerService trainerService,
                             TrainerMapper trainerMapper,
                             TrainerProfileMapper trainerProfileMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
        this.trainerProfileMapper = trainerProfileMapper;
    }

    /**
     * POST request to register a new trainer.
     *
     * @param trainerCreateDto request contains:
     *                         firstName(required)
     *                         lastName(required)
     *                         specialization id(required)
     * @return generated username and password
     */
    @PostMapping()
    @Operation(description = "Registering a new trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully registered a new trainer",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TrainerResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)))
    }
    )
    public ResponseEntity<TrainerResponseDto> registerTrainer(
            @Valid @RequestBody TrainerCreateRequestDto trainerCreateDto) {
        log.debug("Request to register a new trainer: {}", trainerCreateDto);
        TrainerEntity trainer = trainerMapper.dtoToEntity(trainerCreateDto);
        TrainerEntity registeredTrainer = trainerService.registerTrainer(trainer);
        return new ResponseEntity<>(trainerMapper.entityToResponseDto(registeredTrainer), HttpStatus.CREATED);
    }

    /**
     * GET request to return Trainer Profile.
     *
     * @param username username of the Trainer
     * @return {@code TrainerProfileResponseDto}:
     */
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDto> getTrainerProfile(@PathVariable("username") String username) {
        log.debug("Request to get trainer profile with username: {}", username);
        TrainerEntity trainer = trainerService.getTrainerProfile(username);
        return new ResponseEntity<>(trainerProfileMapper.entityToProfileDto(trainer), HttpStatus.OK);
    }

    /**
     * PUT request to update trainer.
     *
     * @param trainer to update: {@code TrainerUpdateRequestDto}:
     *                username(required)
     *                firstName(required)
     *                lastName(required)
     *                specialization(read-only)
     *                isActive(required)
     * @return {@code TrainerUpdateResponseDto}
     */
    @PutMapping()
    public ResponseEntity<TrainerUpdateResponseDto> updateTrainer(
            @Valid @RequestBody TrainerUpdateRequestDto trainer) {
        log.debug("Request to update trainer with username: {}", trainer.getUsername());
        TrainerEntity trainerEntity = trainerMapper.updateDtoToEntity(trainer);
        TrainerEntity updatedTrainer = trainerService.updateTrainer(trainerEntity);
        TrainerUpdateResponseDto trainerResponse = trainerProfileMapper
                .entityToUpdatedDto(updatedTrainer);
        return new ResponseEntity<>(trainerResponse, HttpStatus.OK);
    }

    /**
     * PATCH request to activate/deactivate a trainer.
     *
     * @param activeStatusRequestDto {@code UserChangeActiveStatusRequestDto}: username(required)
     *                               isActive(required)
     */
    @PatchMapping("activation")
    public ResponseEntity<String> changeActiveStatus(@Valid @RequestBody UserChangeActiveStatusRequestDto
                                                             activeStatusRequestDto) {
        log.debug("Request to change the active status of trainer with username: {} to {}",
                activeStatusRequestDto.getUsername(), activeStatusRequestDto.getIsActive());
        String response = trainerService.changeActiveStatus(activeStatusRequestDto.getUsername(),
                activeStatusRequestDto.getIsActive());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * GET request to get active trainers which are not assigned to trainee with the given username.
     *
     * @param traineeUsername username of the trainee.
     * @return {@code Set<TrainerProfileDto>}
     */
    @GetMapping("/unassigned/trainee/{username}")
    public ResponseEntity<Set<TrainerProfileDto>> notAssignedOnTraineeActiveTrainers(
            @PathVariable("username") String traineeUsername) {
        log.debug("Request to get all active trainers which are not assigned to trainee with username: {}",
                traineeUsername);

        return new ResponseEntity<>(trainerService.notAssignedOnTraineeActiveTrainers(traineeUsername)
                .stream()
                .map(trainerMapper::entityToProfileDto).collect(Collectors.toSet()), HttpStatus.OK);
    }

}
