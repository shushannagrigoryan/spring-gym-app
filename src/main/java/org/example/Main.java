package org.example;

import org.example.services.TraineeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ApplicationContext  context = new AnnotationConfigApplicationContext(Config.class);

        TraineeService trainee = context.getBean(TraineeService.class);

        trainee.createTrainee("John", "Smith", "password15",
                LocalDate.now(), "myAddress");


        trainee.createTrainee("Jack", "Johnes", "mypassword",
                LocalDate.now(), "myAddress");

        trainee.createTrainee("Jack", "Johnes", "pass78+-rt",
                LocalDate.now(), "myAddress");

        trainee.createTrainee("Jack", "Johnes", "gnh5t+-r&t",
                LocalDate.now(), "myAddress");

        trainee.createTrainee("Jack", "Johnes", "gnh5t+-r&t",
                LocalDate.now(), "myAddress");







    }
}