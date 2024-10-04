package org.example.controller;

import org.example.dto.TraineeCreateDto;
import org.example.dto.TraineeResponseDto;
import org.example.entity.TraineeEntity;
import org.example.mapper.TraineeMapper;
import org.example.services.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainees")
public class TraineeController {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;

    public TraineeController(TraineeService traineeService, TraineeMapper traineeMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
    }

    /** create trainee.*/
    @PostMapping
    public ResponseEntity<TraineeResponseDto> registerTrainee(@RequestBody TraineeCreateDto traineeCreateDto) {
        TraineeEntity trainee = traineeMapper.dtoToEntity(traineeCreateDto);
        TraineeEntity traineeEntity = traineeService.registerTrainee(trainee);
        return new ResponseEntity<>(traineeMapper.entityToResponseDto(traineeEntity), HttpStatus.CREATED);
    }

    /** try tomcat. */
    @GetMapping
    public void getMethod() {
        System.out.println("get request");
    }
}
