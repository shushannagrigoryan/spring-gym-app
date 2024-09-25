package org.example;

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
        try {
            //            traineeFacade.createTrainee(new TraineeDto(
            //                    "A", "B", LocalDate.now(), "myAddress"));
            //trainerFacade.createTrainer(new TrainerDto("A", "B", 5L));
            //trainerFacade.createTrainer(new TrainerDto("A", "B", 2L));

            //System.out.println(traineeFacade.getTraineeById(100L));
            //System.out.println(traineeFacade.getTraineeById(10L));
            //System.out.println(trainerFacade.getTrainerById(3L));
            //System.out.println(traineeFacade.getTraineeByUsername("A.B"));
            //System.out.println(trainerFacade.getTrainerByUsername("A.B25"));
            //traineeFacade.changeTraineePassword("A.B", "b_%{zrll)0");
            //trainerFacade.changeTrainerPassword("A.B25", "kr^,.cC|<}");
            //traineeFacade.activateTrainee(1L);
            //traineeFacade.activateTrainee(1L);
            //traineeFacade.activateTrainee(10L);
            //            traineeFacade.createTrainee(new TraineeDto("B", "B",
            //                    LocalDate.now(), "address"));

            trainerFacade.getTrainerByUsername("B.B");

        } catch (RuntimeException exception) {
            log.error(exception.getMessage(), exception);
        }

    }
}