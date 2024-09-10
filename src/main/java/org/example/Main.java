package org.example;

import lombok.extern.slf4j.Slf4j;
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

        TrainerFacade trainerFacade = context.getBean(TrainerFacade.class);

        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);



    }
}