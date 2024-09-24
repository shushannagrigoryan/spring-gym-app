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
        try{
            traineeFacade.createTrainee(new TraineeDto(
                    "A", "B", LocalDate.now(), "myAddress"));
            //trainerFacade.createTrainer(new TrainerDto("A", "B", 5L));
            trainerFacade.createTrainer(new TrainerDto("A", "B", 2L));

            //            trainingFacade.createTraining(new TrainingDto(1L, 1L,
            //                    "t1", 1L, LocalDate.now(), BigDecimal.valueOf(60)));
        } catch (RuntimeException exception) {
            log.error(exception.getMessage(),exception);
        }

    }
}