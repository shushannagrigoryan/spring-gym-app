package org.example;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingType;
import org.example.facade.TraineeFacade;
import org.example.facade.TrainerFacade;
import org.example.facade.TrainingFacade;
import org.example.storage.DataStorage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Main {
    /**
     * Main method.
     */
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(DataStorage.class);
        TraineeFacade traineeFacade = context.getBean(TraineeFacade.class);

        traineeFacade.createTrainee(new TraineeDto("John", "Smith",
                "myPassword", LocalDate.now(), "myAddress"));

        //        traineeFacade.createTrainee(new TraineeDto("John", "Smith",
        //                "myPassword", LocalDate.now(), "myAddress"));
        //        traineeFacade.createTrainee(new TraineeDto("John", "Smith",
        //                "myPassword123", LocalDate.now(), "myAddress"));
        //        traineeFacade.createTrainee(new TraineeDto("Sam", "Smith",
        //                "myPassword", LocalDate.now(), "myAddress"));
        //        traineeFacade.createTrainee(new TraineeDto("Ann", "Thompson",
        //                "my8$ssword", LocalDate.now(), "myAddress"));
        //
        //
        TrainerFacade trainerFacade = context.getBean(TrainerFacade.class);
        //        trainerFacade.createTrainer(new TrainerDto("John", "Smith",
        //                "myPassword", "boxing"));
        //        trainerFacade.createTrainer(new TrainerDto("A", "B",
        //                "myPassword", "boxing"));
        //        trainerFacade.createTrainer(new TrainerDto("John", "Smith",
        //                "myPassword", "boxing"));
        //
        //        System.out.println(trainerFacade.getTrainerById(3L));
        //        System.out.println(trainerFacade.getTrainerById(15552L));
        //
        //        System.out.println(traineeFacade.getTraineeById(1L));
        //        System.out.println(traineeFacade.getTraineeById(1000L));
        //
        //        trainerFacade.updateTrainerById(85345L, new TrainerDto());
        //
        //        traineeFacade.deleteTraineeById(2L);
        //        traineeFacade.deleteTraineeById(2000L);
        //        traineeFacade.updateTraineeById(1L, new TraineeDto("Jack", "Smith",
        //                "myPassword", LocalDate.now(), "myAddress"));

        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);


        trainingFacade.createTraining(new TrainingDto(0L, 0L,
                "Boxing", TrainingType.CARDIO, LocalDateTime.now(), Duration.ofHours(1)));
        System.out.println(trainingFacade.getTrainingById(5L));
        System.out.println(trainingFacade.getTrainingById(1000L));

    }
}