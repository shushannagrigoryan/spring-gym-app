//package org.example.aspect;
//
//import java.util.UUID;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.slf4j.MDC;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//@Slf4j
//public class TransactionIdAspect {
//
//    /** Generating transaction id before controller requests. */
//    @Before("execution(* org.example.controller.TraineeController.*(..)) "
//            + "|| execution(* org.example.controller.TrainerController.*(..))"
//            + "|| execution(* org.example.controller.TrainingController.*(..))"
//            + "|| execution(* org.example.controller.TrainingTypeController.*(..))"
//            + "|| execution(* org.example.controller.UserController.*(..))")
//    public void beforeControllerMethod() {
//        log.debug("Generating transaction id.");
//        String transactionId = UUID.randomUUID().toString();
//        MDC.put("transactionId", transactionId);
//        log.debug("Successfully generated transaction ID: " + transactionId);
//    }
//
//}
