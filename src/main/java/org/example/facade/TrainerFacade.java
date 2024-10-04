//package org.example.facade;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.extern.slf4j.Slf4j;
//import org.example.auth.TrainerAuth;
//import org.example.dto.TrainerDto;
//import org.example.entity.TrainerEntity;
//import org.example.mapper.TrainerMapper;
//import org.example.services.TrainerService;
//import org.example.validation.TrainerValidation;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class TrainerFacade {
//    private final TrainerService trainerService;
//    private final TrainerMapper trainerMapper;
//    private final TrainerValidation trainerValidation;
//    private final TrainerAuth trainerAuth;
//
//    /**
//     * Injecting {@code TrainerFacade} dependencies.
//     */
//    public TrainerFacade(TrainerService trainerService,
//                         TrainerMapper trainerMapper,
//                         TrainerValidation trainerValidation,
//                         TrainerAuth trainerAuth) {
//        this.trainerService = trainerService;
//        this.trainerMapper = trainerMapper;
//        this.trainerValidation = trainerValidation;
//        this.trainerAuth = trainerAuth;
//    }
//
//    /**
//     * Creates a new Trainer in the facade layer.
//     *
//     * @param trainerDto {@code TrainerDto} to create the {@code TrainerEntity}
//     */
//    public void createTrainer(TrainerDto trainerDto) {
//        log.info("Request to create trainer");
//        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
//        trainerValidation.validateTrainer(trainerDto);
//        trainerService.createTrainer(trainerEntity);
//    }
//
//    /**
//     * Gets the trainer by id.
//     *
//     * @param id the id of the trainer
//     * @return the TrainerDto
//     */
//    public TrainerDto getTrainerById(Long id) {
//        log.info("Request to retrieve trainer by id");
//        TrainerEntity trainer = trainerService.getTrainerById(id);
//        return trainerMapper.entityToDto(trainer);
//    }
//
//
//    /**
//     * Gets trainer by username.
//     *
//     * @param username username of the trainer
//     * @return {@code TrainerDto}
//     */
//    public TrainerDto getTrainerByUsername(String username) {
//        log.info("Request to retrieve trainer by username");
//        TrainerEntity trainer = trainerService.getTrainerByUsername(username);
//        return trainerMapper.entityToDto(trainer);
//    }
//
//    /**
//     * Changes trainer password.
//     *
//     * @param username username of the trainer
//     * @param password password of the trainer
//     */
//
//    public void changeTrainerPassword(String username, String password) {
//        log.info("Request to change trainer password.");
//        if (trainerAuth.trainerAuth(username, password)) {
//            trainerService.changeTrainerPassword(username);
//        }
//    }
//
//    /**
//     * Updates Trainer by id.
//     *
//     * @param id         id of the trainer to update
//     * @param trainerDto new trainer data to update with
//     */
//    public void updateTrainerById(Long id, TrainerDto trainerDto) {
//        log.info("Request to update trainer by id");
//        trainerValidation.validateTrainer(trainerDto);
//        trainerService.updateTrainerById(id, trainerMapper.dtoToEntity(trainerDto));
//    }
//
//    /**
//     * Activates trainer.
//     *
//     * @param id id of the Trainer
//     */
//    public void activateTrainer(Long id) {
//        log.info("Request to activate trainer with id: {}", id);
//        trainerService.activateTrainer(id);
//    }
//
//    /**
//     * Deactivates trainer.
//     *
//     * @param id id of the Trainer
//     */
//    public void deactivateTrainer(Long id) {
//        log.info("Request to deactivate trainer with id: {}", id);
//        trainerService.deactivateTrainer(id);
//    }
//
//    /**
//     * Returns trainers not assigned to trainee by trainee username.
//     *
//     * @param traineeUsername of the trainee.
//     * @return {@code List<TrainerDto>}
//     */
//    public List<TrainerDto> getTrainersNotAssignedToTrainee(String traineeUsername) {
//        log.info("Request to get trainers not assigned to trainee with username: {}", traineeUsername);
//        List<TrainerEntity> trainers = trainerService.getTrainersNotAssignedToTrainee(traineeUsername);
//        return trainers.stream().map(trainerMapper::entityToDto).collect(Collectors.toList());
//    }
//}
