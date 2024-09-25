package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.example.config.HibernateConfig;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
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
        try {
            traineeFacade.createTrainee(new TraineeDto(
                    "Mike", "Jones", LocalDate.now(), "myAddress"));

            trainerFacade.createTrainer(new TrainerDto("Tom", "A", 1L));
            trainerFacade.createTrainer(new TrainerDto("Mike", "B", 2L));
            trainerFacade.createTrainer(new TrainerDto("A", "F", 1L));

            trainingFacade.createTraining(new TrainingDto(2L, 2L,
                    "tName", 2L, LocalDate.now(), BigDecimal.valueOf(60)));

            trainingFacade.createTraining(new TrainingDto(3L, 4L,
                    "tName", 1L, LocalDate.now(), BigDecimal.valueOf(60)));
            trainingFacade.createTraining(new TrainingDto(1L, 2L,
                    "tName", 1L, LocalDate.now(), BigDecimal.valueOf(60)));
            //            trainingFacade.createTraining(new TrainingDto(1L, 6L,
            //                    "tName", 3L, LocalDate.now(), BigDecimal.valueOf(60)));
            //System.out.println(traineeFacade.getTraineeById(100L));
            //System.out.println(traineeFacade.getTraineeById(10L));
            //System.out.println(trainerFacade.getTrainerById(3L));
            //System.out.println(traineeFacade.getTraineeByUsername("A.B"));
            //System.out.println(trainerFacade.getTrainerByUsername("A.B25"));
            //traineeFacade.changeTraineePassword("A.B", "<[l[JY/.b>");
            //trainerFacade.changeTrainerPassword("A.B25", " -#s6#4H+S");
            //traineeFacade.changeTraineePassword("A.B", "b_%{zrll)0");
            //trainerFacade.changeTrainerPassword("A.B25", "kr^,.cC|<}");
            //traineeFacade.activateTrainee(3L);
            //trainerFacade.activateTrainer(5L);
            //trainerFacade.deactivateTrainer(5L);
            //traineeFacade.deactivateTrainee(3L);
            //traineeFacade.activateTrainee(63L);
            //            traineeFacade.createTrainee(new TraineeDto("B", "B",
            //                    LocalDate.now(), "address"));

            //trainerFacade.getTrainerByUsername("B.B");
            //            traineeFacade.updateTraineeById(1L, new TraineeDto(
            //                    "Jack", "Smith",
            //                    LocalDate.of(2024, 7, 8), "newAddress"));

            //trainerFacade.updateTrainerById(1L, new TrainerDto("A", "M", 2L));
            //traineeFacade.deleteTraineeByUsername("A.B32");
            //            trainingFacade.createTraining(new TrainingDto(2L, 1L,
            //                    "tName", 2L, LocalDate.of(2024, 7, 7), BigDecimal.valueOf(60)));

            //            System.out.println(traineeFacade.getTraineeTrainingsByFilter(
            //                    "A.B1", LocalDate.of(2024, 6, 1),
            //                    LocalDate.of(2024, 7, 9), null, null
            //            ));
            //            System.out.println(traineeFacade.getTraineeTrainingsByFilter(
            //                    "A.B1", null,
            //                    null, null, "A.M"
            //            ));
            //
            //            System.out.println(trainerFacade.getTrainerTrainingsByFilter(
            //                    "A.M", LocalDate.of(2024, 6, 1),
            //                    LocalDate.of(2024, 9, 25), "A.B1"
            //            ));
            //            trainingFacade.updateTraining(2L, new TrainingDto(
            //                    4L, 7L, "newtName",
            //                    1L, LocalDate.now(), BigDecimal.valueOf(90)
            //            ));
            //
            //            System.out.println(trainerFacade.getTrainersNotAssignedToTrainee("A.B1"));
            //traineeFacade.getTraineeByUsername()
        } catch (RuntimeException exception) {
            log.error(exception.getMessage(), exception);
        }

    }
}