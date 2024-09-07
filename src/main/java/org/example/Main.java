package org.example;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingType;
import org.example.exceptions.GymIllegalIdException;
import org.example.facade.TraineeFacade;
import org.example.facade.TrainerFacade;
import org.example.facade.TrainingFacade;
import org.example.storage.DataStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Main method.
     */
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(DataStorage.class);
        TraineeFacade traineeFacade = context.getBean(TraineeFacade.class);
        traineeFacade.createTrainee(new TraineeDto("John", "Smith",
                "myPassword", LocalDate.now(), "myAddress"));
        traineeFacade.createTrainee(new TraineeDto("John", "Smith",
                "myPassword123", LocalDate.now(), "myAddress"));
        traineeFacade.createTrainee(new TraineeDto("Sam", "Smith",
                "myPassword", LocalDate.now(), "myAddress"));


        traineeFacade.createTrainee(new TraineeDto("Ann", "Thompson",
                "myPassword", LocalDate.now(), "myAddress"));

        //        TraineeDto traineeDto1 = null;
        //        try {
        //            traineeDto1 = traineeFacade.getTraineeById(1000L);
        //        } catch (GymIllegalIdException exception) {
        //            logger.error(exception.getMessage(), exception);
        //        }
        //        System.out.println(traineeDto1);

        //traineeFacade.deleteTraineeById(2000L);
        //traineeFacade.deleteTraineeById(5L);
        //        traineeFacade.updateTraineeById(5000L, new TraineeDto("Ann", "Tompson",
        //                "myPassword", LocalDate.now(), "myAddress"));
        //        traineeFacade.updateTraineeById(2L, new TraineeDto("Ann", "Tompson",
        //                "myPasswordefe", LocalDate.now(), "myAddress"));
        //        traineeFacade.updateTraineeById(3L, new TraineeDto("Tom", "Tompson",
        //                "myPassword", LocalDate.now(), "myNewAddress"));


        TrainerFacade trainerFacade = context.getBean(TrainerFacade.class);
        trainerFacade.createTrainer(new TrainerDto("A", "B",
                "myPassword", "boxing"));
        trainerFacade.createTrainer(new TrainerDto("John", "Smith",
                "myPassword", "boxing"));


        TrainerDto trainerDto = null;
        try {
            trainerDto = trainerFacade.getTrainerById(2L);
        } catch (GymIllegalIdException exception) {
            logger.error(exception.getMessage(), exception);
        }
        System.out.println(trainerDto);


        //        trainerFacade.updateTrainerById(3L, new TrainerDto("E", "E",
        //                "MyPassword", "boxing"));

        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);
        //        trainingFacade.createTraining(new TrainingDto(80L, 600L,
        //                "Boxing", TrainingType.CARDIO, LocalDateTime.now(), Duration.ofHours(1)));
        //        trainingFacade.createTraining(new TrainingDto(10L, 10L,
        //                "Boxing", TrainingType.CARDIO, LocalDateTime.now(), Duration.ofHours(1)));
        //
        //        TrainingDto trainingDto = null;
        //        try {
        //            trainingDto = trainingFacade.getTrainingById(2L);
        //        } catch (GymIllegalIdException exception) {
        //            logger.error(exception.getMessage(), exception);
        //        }
        //        System.out.println(trainingDto);

        trainingFacade.createTraining(new TrainingDto(0L, 0L,
                "Boxing", TrainingType.CARDIO, LocalDateTime.now(), Duration.ofHours(1)));

    }
}