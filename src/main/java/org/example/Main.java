package org.example;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.example.config.HibernateConfig;
import org.example.dto.TraineeDto;
import org.example.facade.TraineeFacade;
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

        traineeFacade.createTrainee(new TraineeDto("John", "Smith",
                LocalDate.now(), "myAddress1234"));
        //System.out.println(traineeFacade.getTraineeById(4L));
        //System.out.println(traineeFacade.getTraineeById(50L));
    }
}