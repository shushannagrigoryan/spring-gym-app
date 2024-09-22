package org.example;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.example.config.HibernateConfig;
import org.example.facade.TraineeFacade;
import org.example.facade.TrainerFacade;
import org.example.facade.TrainingFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Main {
    /**
     * Main method.
     */
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        TraineeFacade traineeFacade = context.getBean(TraineeFacade.class);
        TrainerFacade trainerFacade = context.getBean(TrainerFacade.class);
        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);
        //        traineeFacade.createTrainee(new TraineeDto("Mike", "Smith",
        //                LocalDate.now(), "myAddress1234"));
        //        trainerFacade.createTrainer(
        //                new TrainerDto("Mike", "Smith",
        //                        new TrainingTypeDto("yoga")));

        //System.out.println(traineeFacade.getTraineeById(4L));
        //System.out.println(traineeFacade.getTraineeById(50L));

        //        trainerFacade.createTrainer(new TrainerDto("fname", "lname",
        //                new TrainingTypeDto("boxing")));

        //        trainingFacade.createTraining(new TrainingDto(1L, 4L,
        //                "trainingName1", 2L, LocalDateTime.now(), Duration.ofHours(1)));

        //        System.out.println(trainerFacade.getTrainerByUsername("A.A,"));
        //        System.out.println(trainerFacade.getTrainerByUsername("Mike.Smith3"));

        //        trainerFacade.createTrainer(new TrainerDto("A", "B",
        //                "swimming"));

        //        traineeFacade.changeTraineePassword(
        //                "Mike.Smith", "$]6HtX!e5j");

        //        traineeFacade.changeTraineePassword("Mike.Smith4", "&oL3W_U KL");
        //        traineeFacade.activateTrainee(1L);



        //        trainerFacade.createTrainer(new TrainerDto("A", "B", 2L));
        //        traineeFacade.createTrainee(new TraineeDto(
        //                "D", "D", LocalDate.now(), "myAddress"
        //        ));
        //        traineeFacade.createTrainee(new TraineeDto(
        //                "A", "B", LocalDate.now(), "myAddress"
        //        ));

        //
        //        trainingFacade.createTraining(new TrainingDto(1L, 1L,
        //                "trainingname sdksnd",
        //                1L, LocalDateTime.now(), Duration.ofHours(1)));

        //        trainingFacade.createTraining(new TrainingDto(
        //                1L, 3L, "newTrainingName",
        //                2L, LocalDateTime.now(), Duration.ofHours(1)
        //        ));

        //        traineeFacade.updateTraineeById(1L, new TraineeDto(
        //                "T", "T", LocalDate.now(), "myNewAddress"
        //        ));

        //        trainerFacade.updateTrainerById(1L,
        //                new TrainerDto("A", "B", 2L));

        //System.out.println(traineeFacade.trainersNotAssignedToTrainee("T.T"));

        //traineeFacade.activateTrainee(3L);
        //traineeFacade.deactivateTrainee(3L);

        //trainerFacade.activateTrainer(3L);
        //trainerFacade.deactivateTrainer(3L);




        //        traineeFacade.
        //                createTrainee(new TraineeDto("A", "B",
        //                        LocalDate.now(), "myAddress"));
        //
        //
        //        trainerFacade.createTrainer(new TrainerDto("C", "D", 2L));
        //
        //        trainingFacade.createTraining(new TrainingDto(
        //                2L, 5L, "trainingName",
        //                2L, LocalDate.of(2024, 2, 5),
        //                BigDecimal.valueOf(40)
        //        ));

        System.out.println(traineeFacade.getTraineeTrainingsByFilter("A.B1",
                LocalDate.of(2024, 2, 5),
                LocalDate.of(2024, 9, 22), 2L, "C.D2"));


        //traineeFacade.deleteTraineeByUsername("A.B10");



    }
}