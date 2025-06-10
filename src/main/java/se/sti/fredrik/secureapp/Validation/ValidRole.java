package se.sti.fredrik.secureapp.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Documented
@Constraint(validatedBy = ValidRoleChecker.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidRole {
    String message() default "Invalid role provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
