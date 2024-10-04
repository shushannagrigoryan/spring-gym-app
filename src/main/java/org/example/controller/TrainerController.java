package org.example.controller;

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
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    /** Setting dependencies. */
    public TrainerController(TrainerService trainerService,
                             TrainerMapper trainerMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

    /** create trainer. */
    @PostMapping
    public ResponseEntity<TrainerResponseDto> registerTrainer(@RequestBody TrainerCreateDto trainerCreateDto) {
        TrainerEntity trainer = trainerMapper.dtoToEntity(trainerCreateDto);
        TrainerEntity trainerEntity = trainerService.registerTrainer(trainer);
        return new ResponseEntity<>(trainerMapper.entityToResponseDto(trainerEntity), HttpStatus.CREATED);
    }
}
