package org.example.auth;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.example.exceptions.GymIllegalArgumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TrainerAuthAspect {
    private final TrainerAuth trainerAuth;
    @Value("${trainer-auth.username}")
    private String username;
    @Value("${trainer-auth.password}")
    private String password;

    public TrainerAuthAspect(TrainerAuth trainerAuth) {
        this.trainerAuth = trainerAuth;
    }

    /**
     * Before the specified methods perform trainer authentication.
     */
    //    @Before("execution(* org.example.facade.TrainerFacade.*(..)) "
    //            + "&& !execution(* org.example.facade.TrainerFacade.createTrainer(..))"
    //            + "|| execution(* org.example.facade.TrainingFacade.*(..))"
    //            + "|| execution(* org.example.facade.TrainingTypeFacade.*(..))")
    public void trainerAuthentication() {
        log.info("Authenticating trainer: username: {} password: {}",
                username, password);

        try {
            trainerAuth.trainerAuth(username, password);
        } catch (GymIllegalArgumentException exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }


}
