package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TrainerCreateRequestDto;
import org.example.dto.requestdto.TrainerUpdateRequestDto;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.requestdto.UserChangeActiveStatusRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.dto.responsedto.TrainerProfileDto;
import org.example.dto.responsedto.TrainerProfileResponseDto;
import org.example.dto.responsedto.TrainerResponseDto;
import org.example.dto.responsedto.TrainerUpdateResponseDto;
import org.example.entity.TrainerEntity;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainerProfileMapper;
import org.example.metrics.TrainerRequestMetrics;
import org.example.services.TrainerService;
import org.example.services.TrainerWorkloadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainers")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "TrainerController")
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final TrainerProfileMapper trainerProfileMapper;
    private final TrainerRequestMetrics trainerRequestMetrics;
    private final TrainerWorkloadService trainerWorkloadService;

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
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "201",
                description = "Successfully registered a new trainer.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    public ResponseEntity<ResponseDto<TrainerResponseDto>> registerTrainer(
        @Valid @RequestBody TrainerCreateRequestDto trainerCreateDto) {
        trainerRequestMetrics.incrementCounter();
        log.debug("Request to register a new trainer: {}", trainerCreateDto);
        TrainerEntity trainer = trainerMapper.dtoToEntity(trainerCreateDto);
        return new ResponseEntity<>(new ResponseDto<>(trainerService.registerTrainer(trainer),
            "Successfully registered a new trainer."), HttpStatus.CREATED);
    }

    /**
     * GET request to return Trainer Profile.
     *
     * @param username username of the Trainer
     * @return {@code TrainerProfileResponseDto}:
     */
    @GetMapping("/{username}")
    @Operation(description = "Getting trainer profile")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully received trainer profile.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    @PreAuthorize("hasRole('TRAINER') and #username == authentication.name")
    public ResponseEntity<ResponseDto<TrainerProfileResponseDto>> getTrainerProfile(
        @PathVariable("username") String username) {
        trainerRequestMetrics.incrementCounter();
        log.debug("Request to get trainer profile with username: {}", username);
        TrainerEntity trainer = trainerService.getTrainerProfile(username);
        return new ResponseEntity<>(new ResponseDto<>(trainerProfileMapper.entityToProfileDto(trainer),
            "Successfully retrieved trainer profile."), HttpStatus.OK);
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
    @PutMapping("/{username}")
    @Operation(description = "Updating trainer profile")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully updated trainer profile.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    @PreAuthorize("hasRole('TRAINER') and #username == authentication.name")
    public ResponseEntity<ResponseDto<TrainerUpdateResponseDto>> updateTrainer(
        @PathVariable("username") String username,
        @Valid @RequestBody TrainerUpdateRequestDto trainer) {
        trainerRequestMetrics.incrementCounter();
        log.debug("Request to update trainer with username: {}", username);
        TrainerEntity trainerEntity = trainerMapper.updateDtoToEntity(username, trainer);
        TrainerEntity updatedTrainer = trainerService.updateTrainer(trainerEntity);
        TrainerUpdateResponseDto payload = trainerProfileMapper
            .entityToUpdatedDto(updatedTrainer);
        return new ResponseEntity<>(new ResponseDto<>(payload,
            "Successfully updated trainer profile."), HttpStatus.OK);
    }

    /**
     * PATCH request to activate/deactivate a trainer.
     *
     * @param activeStatusRequestDto {@code UserChangeActiveStatusRequestDto}: username(required)
     *                               isActive(required)
     */
    @PatchMapping("/{username}/activation")
    @Operation(description = "Activating/Deactivating trainee")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully changed trainer active status."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    @PreAuthorize("hasRole('TRAINER') and #username == authentication.name")
    public ResponseEntity<ResponseDto<Object>> changeActiveStatus(
        @PathVariable("username") String username,
        @Valid @RequestBody UserChangeActiveStatusRequestDto activeStatusRequestDto) {
        trainerRequestMetrics.incrementCounter();
        log.debug("Request to change the active status of trainer with username: {} to {}",
            username, activeStatusRequestDto.getIsActive());
        String response = trainerService.changeActiveStatus(username,
            activeStatusRequestDto.getIsActive());
        return new ResponseEntity<>(new ResponseDto<>(null, response), HttpStatus.OK);
    }


    /**
     * GET request to get active trainers which are not assigned to trainee with the given username.
     *
     * @param traineeUsername username of the trainee.
     * @return {@code Set<TrainerProfileDto>}
     */
    @GetMapping("/unassigned/trainee/{username}")
    @Operation(description = "Getting not assigned on trainee active trainers.")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully got not assigned on trainee active trainers.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    @PreAuthorize("hasRole('TRAINEE') and #traineeUsername == authentication.name")
    public ResponseEntity<ResponseDto<Set<TrainerProfileDto>>> notAssignedOnTraineeActiveTrainers(
        @PathVariable("username") String traineeUsername) {
        trainerRequestMetrics.incrementCounter();
        log.debug("Request to get all active trainers which are not assigned to trainee with username: {}",
            traineeUsername);

        Set<TrainerProfileDto> payload = trainerService.notAssignedOnTraineeActiveTrainers(traineeUsername)
            .stream()
            .map(trainerMapper::entityToProfileDto).collect(Collectors.toSet());
        return new ResponseEntity<>(new ResponseDto<>(payload,
            "Successfully retrieved active trainers which are not assigned to trainee."),
            HttpStatus.OK);
    }


    /**
     * getting trainer workload.
     */
    @GetMapping("/workload")
    public ResponseEntity<ResponseDto<String>> getTrainerWorkload(
        @RequestParam("username") String username,
        @RequestParam("year") String year,
        @RequestParam("month") String month) {
        log.debug("Request to get trainer : {} workload by month: {}",
            username, year + ":" + month);

        BigDecimal payload = trainerWorkloadService.getTrainerWorkload(new TrainerWorkloadRequestDto(
            username, year, month
        ));

        return new ResponseEntity<>(new ResponseDto<>(payload.toString(),
            "Successfully retrieved trainers workload."),
            HttpStatus.OK);
    }

}
