package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerCreateDto;
import org.example.dto.TrainerResponseDto;
import org.example.entity.TrainerEntity;
import org.example.mapper.TrainerMapper;
import org.example.services.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /** Setting dependencies. */
    public TrainerController(TrainerService trainerService,
                             TrainerMapper trainerMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
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


}
