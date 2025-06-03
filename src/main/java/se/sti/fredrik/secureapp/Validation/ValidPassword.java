package se.sti.fredrik.secureapp.Validation;
//? Denna klass ska kolla att lösenordet följer det som behövs
//? Alltså:
//? 1 Stor bokstav, 2 siffor, 2 specialtecken.
//? Längre fram kommer vi att även använda DTO för validation

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Documented
@Constraint(validatedBy = ValidPasswordChecker.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)

public @interface ValidPassword {
    String message() default "Lösenordet måste vara minst 8 tecken, innehålla 1 stor bokstav, 2 siffror och 2 specialtecken";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
