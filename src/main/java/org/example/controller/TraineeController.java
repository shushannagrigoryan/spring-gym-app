package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeActivateDto;
import org.example.dto.TraineeCreateDto;
import org.example.dto.TraineeProfileResponseDto;
import org.example.dto.TraineeResponseDto;
import org.example.entity.TraineeEntity;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TraineeProfileMapper;
import org.example.services.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainees")
@Slf4j
public class TraineeController {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TraineeProfileMapper traineeProfileMapper;

    /** Setting dependencies.*/
    public TraineeController(TraineeService traineeService,
                             TraineeMapper traineeMapper,
                             TraineeProfileMapper traineeProfileMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
        this.traineeProfileMapper = traineeProfileMapper;
    }

    /**
     * Registers a new trainee.
     *
     * @param traineeCreateDto trainee(firstName, lastName, dateOfBirth, address) to register.
     * @return the saved trainee(username, password)
     */
    @PostMapping
    public ResponseEntity<TraineeResponseDto> registerTrainee(@RequestBody TraineeCreateDto traineeCreateDto) {
        log.debug("Registering new trainee: {}", traineeCreateDto);
        TraineeEntity trainee = traineeMapper.dtoToEntity(traineeCreateDto);
        TraineeEntity traineeEntity = traineeService.registerTrainee(trainee);
        return new ResponseEntity<>(traineeMapper.entityToResponseDto(traineeEntity), HttpStatus.CREATED);
    }

    //    @PutMapping
    //    public void changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
    //        log.debug("Change password of trainee with username: {} ", changePasswordDto.getUsername());
    //
    //    }

    //    @GetMapping
    //    public ResponseEntity<TraineeProfileDto> getTrainee(@RequestBody String username) {
    //        log.debug("Getting trainee with username: {}", username);
    //        TraineeEntity trainee = traineeService.getTraineeByUsername(username);
    //        //TraineeProfileDto trainee = new TraineeProfileDto();
    //        return new ResponseEntity<>(trainee, HttpStatus.OK);
    //    }

    @DeleteMapping
    public void deleteTrainee(@RequestBody String username) {
        log.debug("Request to delete trainee with username: {}", username);

    }

    /** activate traeinne. */
    @PatchMapping
    public void activateTrainee(@RequestBody TraineeActivateDto traineeActivateDto) {
        log.debug("Request to activate trainee with username: {}", traineeActivateDto.getUsername());
        //traineeService.activateTrainee(traineeActivateDto.getUsername());

    }

    /**get trainee profile. */
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponseDto> getTrainee(@PathVariable("username") String username) {
        log.debug("Request to get trainee profile with username: {}", username);
        TraineeEntity trainee = traineeService.getTraineeProfile(username);
        return new ResponseEntity<>(traineeProfileMapper.entityToProfileDto(trainee), HttpStatus.OK);
    }



}
