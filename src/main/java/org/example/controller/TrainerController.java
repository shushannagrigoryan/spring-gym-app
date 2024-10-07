package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerCreateDto;
import org.example.dto.TrainerProfileResponseDto;
import org.example.dto.TrainerResponseDto;
import org.example.entity.TrainerEntity;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainerProfileMapper;
import org.example.services.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
     * Registers a new trainer.
     *
     * @param trainerCreateDto trainer(firstName, lastName, specialization) to register.
     * @return the saved trainer(username, password)
     */
    @PostMapping
    public ResponseEntity<TrainerResponseDto> registerTrainer(@RequestBody TrainerCreateDto trainerCreateDto) {
        log.debug("Registering new trainer: {}", trainerCreateDto);
        TrainerEntity trainer = trainerMapper.dtoToEntity(trainerCreateDto);
        TrainerEntity trainerEntity = trainerService.registerTrainer(trainer);
        return new ResponseEntity<>(trainerMapper.entityToResponseDto(trainerEntity), HttpStatus.CREATED);
    }

    /**get trainer profile. */
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDto> getTrainer(@PathVariable("username") String username) {
        log.debug("Request to get trainer profile with username: {}", username);
        TrainerEntity trainer = trainerService.getTrainerProfile(username);
        return new ResponseEntity<>(trainerProfileMapper.entityToProfileDto(trainer), HttpStatus.OK);
    }
}
