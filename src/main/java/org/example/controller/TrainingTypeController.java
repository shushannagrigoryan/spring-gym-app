package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.responsedto.ResponseDto;
import org.example.dto.responsedto.TrainingTypeResponseDto;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.mapper.TrainingTypeMapper;
import org.example.metrics.TrainingTypeRequestMetrics;
import org.example.services.TrainingTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainingTypes")
@Slf4j
@Tag(name = "TrainingTypeController")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;
    private final TrainingTypeRequestMetrics trainingTypeRequestMetrics;

    /**
     * Setting dependencies.
     */
    public TrainingTypeController(TrainingTypeService trainingTypeService,
                                  TrainingTypeMapper trainingTypeMapper,
                                  TrainingTypeRequestMetrics trainingTypeRequestMetrics) {
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeMapper = trainingTypeMapper;
        this.trainingTypeRequestMetrics = trainingTypeRequestMetrics;
    }

    /**
     * GET request to get all training types.
     *
     * @return {@code TrainingTypeResponseDto}
     */
    @GetMapping
    @Operation(description = "Getting training types.")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully got training types.",
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
    public ResponseEntity<ResponseDto<List<TrainingTypeResponseDto>>> getTrainingTypes() {
        trainingTypeRequestMetrics.incrementCounter();
        log.debug("Request to get all training types.");
        List<TrainingTypeEntity> trainingTypes = trainingTypeService.getAllTrainingTypes();
        List<TrainingTypeResponseDto> payload =
            trainingTypes.stream()
                .map(trainingTypeMapper::entityToResponseDto).toList();
        return new ResponseEntity<>(new ResponseDto<>(payload, "Successfully retrieved training types."),
            HttpStatus.OK);
    }
}
