package org.example.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.example.constraints.CustomDateTimeConstraint;

@Slf4j
public class DateTimeFormatValidator implements ConstraintValidator<CustomDateTimeConstraint, String> {

    @Override
    public boolean isValid(String dateTimeValue, ConstraintValidatorContext context) {
        if (dateTimeValue == null) {
            return true;
        }

        try {
            LocalDateTime date = LocalDateTime.parse(dateTimeValue);
            log.debug("DateTime validation succeeded for: {}", date);
            return true;
        } catch (DateTimeParseException e) {
            log.debug("DateTime validation failed for value: {}", dateTimeValue, e);
            return false;
        }
    }
}
