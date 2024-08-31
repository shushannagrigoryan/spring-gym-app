package org.example;

import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.services.TrainingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

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

        //trainee.deleteTrainee("JackJohnes");

        trainee.createTrainee("Sam", "Smith", "password%^",
                LocalDate.of(1990, Month.OCTOBER, 8),
                "myAddress");


        TrainerService trainer = context.getBean(TrainerService.class);
        trainer.createTrainer("A", "B", "mypassword", "boxing trainer");
        trainer.createTrainer("A", "B", "mypassword", "pilates trainer");

        TrainingService training = context.getBean(TrainingService.class);
        training.createTraining("JackJohnes", "AB", "boxing",
                TrainingType.CARDIO, LocalDateTime.now(), Duration.ofHours(1));

        System.out.println(training.getTraining(0L));





    }
}