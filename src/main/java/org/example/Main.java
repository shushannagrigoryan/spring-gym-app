package org.example;

import org.example.dto.TraineeDto;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalUsernameException;
import org.example.facade.TraineeFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        ApplicationContext  context = new AnnotationConfigApplicationContext(Config.class);
        TraineeFacade traineeFacade = context.getBean(TraineeFacade.class);
        traineeFacade.createTrainee(new TraineeDto("John", "Smith",
                "myPassword", LocalDate.now(), "myAddress"));
        traineeFacade.createTrainee(new TraineeDto("John", "Smith",
                "myPassword123", LocalDate.now(), "myAddress"));
        traineeFacade.createTrainee(new TraineeDto("Sam", "Smith",
                "myPassword", LocalDate.now(), "myAddress"));

        TraineeDto traineeDto = null;
        try{
            traineeDto = traineeFacade.getTraineeByUsername("AB");
        }catch (IllegalUsernameException exception){
            logger.error(exception.getMessage(), exception);
//            exception.printStackTrace();
        }
        System.out.println(traineeDto);

        traineeFacade.createTrainee(new TraineeDto("Ann", "Tompson",
                "myPassword", LocalDate.now(), "myAddress"));

        TraineeDto traineeDto1 = null;
        try{
            traineeDto1 = traineeFacade.getTraineeById(1000L);
        }catch (IllegalIdException exception){
            logger.error(exception.getMessage(), exception);
//            exception.printStackTrace();
        }
        System.out.println(traineeDto1);

        traineeFacade.deleteTraineeById(2000L);
        traineeFacade.deleteTraineeById(5L);
        traineeFacade.updateTraineeById(5000L, new TraineeDto("Ann", "Tompson",
                "myPassword", LocalDate.now(), "myAddress"));
        traineeFacade.updateTraineeById(2L, new TraineeDto("Ann", "Tompson",
                "myPasswordefe", LocalDate.now(), "myAddress"));
        traineeFacade.updateTraineeById(3L, new TraineeDto("Tom", "Tompson",
                "myPassword", LocalDate.now(), "myNewAddress"));



//
//        TraineeService trainee = context.getBean(TraineeService.class);
//
//        trainee.createTrainee("John", "Smith", "password15",
//                LocalDate.now(), "myAddress");
//
//
//        trainee.createTrainee("Jack", "Johnes", "mypassword",
//                LocalDate.now(), "myAddress");
//
//        trainee.createTrainee("Jack", "Johnes", "pass78+-rt",
//                LocalDate.now(), "myAddress");
//
////        trainee.createTrainee("Jack", "Johnes", "gnh5t+-r&t",
////                LocalDate.now(), "myAddress");
////
////        trainee.createTrainee("Jack", "Johnes", "gnh5t+-r&t",
////                LocalDate.now(), "myAddress");
//
//        //trainee.deleteTrainee("JackJohnes");
//
//        trainee.createTrainee("Sam", "Smith", "password%^",
//                LocalDate.of(1990, Month.OCTOBER, 8),
//                "myAddress");
//
//
//        TrainerService trainer = context.getBean(TrainerService.class);
//        trainer.createTrainer("A", "B", "mypassword", "boxing trainer");
//        trainer.createTrainer("A", "B", "mypassword", "pilates trainer");
//        trainer.createTrainer("Jack", "Johnes", "mypassword", "pilates trainer");
//
//        TrainingService training = context.getBean(TrainingService.class);
//        training.createTraining("JackJohnes", "AB", "boxing",
//                TrainingType.CARDIO, LocalDateTime.of(2024, 8, 30, 13,30), Duration.ofMinutes(70));
//
//
//        training.createTraining("SamSmith", "JackJohnes1", "pilates",
//                TrainingType.FLEXIBILITY, LocalDateTime.of(2024, 8, 30, 13,30), Duration.ofMinutes(70));
//        System.out.println(training.getTraining(0L));





    }
}