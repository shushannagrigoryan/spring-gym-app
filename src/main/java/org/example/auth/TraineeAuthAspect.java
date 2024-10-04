//package org.example.auth;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.example.exceptions.GymIllegalArgumentException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//@Slf4j
//public class TraineeAuthAspect {
//    private final TraineeAuth traineeAuth;
//    @Value("${trainee-auth.username}")
//    private String username;
//    @Value("${trainee-auth.password}")
//    private String password;
//
//    public TraineeAuthAspect(TraineeAuth traineeAuth) {
//        this.traineeAuth = traineeAuth;
//    }
//
//    /**
//     * Perform trainee authentication before the specified methods.
//     */
//    @Before("execution(* org.example.facade.TraineeFacade.*(..)) "
//            + "&& !execution(* org.example.facade.TraineeFacade.createTrainee(..))"
//            + "|| execution(* org.example.facade.TrainingFacade.*(..))"
//            + "|| execution(* org.example.facade.TrainingTypeFacade.*(..))")
//    public void traineeAuthentication() {
//        log.info("Authenticating trainee: username: {} password: {}", username, password);
//        try {
//            traineeAuth.traineeAuth(username, password);
//        } catch (GymIllegalArgumentException exception) {
//            log.error(exception.getMessage(), exception);
//            throw exception;
//        }
//    }
//
//
//}
