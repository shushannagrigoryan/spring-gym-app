package org.example.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerCreateDto;
import org.example.dto.TrainerProfileResponseDto;
import org.example.dto.TrainerResponseDto;
import org.example.dto.TrainerUpdateRequestDto;
import org.example.dto.TrainerUpdateResponseDto;
import org.example.entity.TrainerEntity;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainerProfileMapper;
import org.example.services.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainers")
@Slf4j
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final TrainerProfileMapper trainerProfileMapper;

    /** Setting dependencies. */
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
    @PostMapping("/register")
    public ResponseEntity<TrainerResponseDto> registerTrainer(@Valid @RequestBody TrainerCreateDto trainerCreateDto) {
        log.debug("Request to register a new trainer: {}", trainerCreateDto);
        TrainerEntity trainer = trainerMapper.dtoToEntity(trainerCreateDto);
        TrainerEntity registeredTrainer = trainerService.registerTrainer(trainer);
        return new ResponseEntity<>(trainerMapper.entityToResponseDto(registeredTrainer), HttpStatus.CREATED);
    }

    /**get trainer profile. */
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
    @PutMapping("/update-trainer")
    public ResponseEntity<TrainerUpdateResponseDto> updateTrainee(
            @Valid @RequestBody TrainerUpdateRequestDto trainer) {
        log.debug("Request to update trainer with username: {}", trainer.getUsername());
        TrainerEntity trainerEntity = trainerMapper.updateDtoToEntity(trainer);
        TrainerEntity updatedTrainer = trainerService.updateTrainer(trainerEntity);
        TrainerUpdateResponseDto trainerResponse = trainerProfileMapper
                .entityToUpdatedDto(updatedTrainer);
        return new ResponseEntity<>(trainerResponse, HttpStatus.OK);
    }
}
