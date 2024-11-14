package org.example.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.example.validators.DateTimeFormatValidator;

@Documented
@Constraint(validatedBy = DateTimeFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDateTimeConstraint {
    /**
     * Default message.
     */
    String message() default "Wrong date format. Correct format is: yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Validation payload.
     */
    Class<? extends Payload>[] payload() default {};
}