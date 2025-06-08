package se.sti.fredrik.secureapp.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Custom annotation used to validate passwords according to rules
 * <p>A valid password must include:</p>
 * <ul>
 *     <li>Minimum length of 8 characters</li>
 *     <li>At least 1 uppercase letter</li>
 *     <li>At least 2 special characters</li>
 *     <li>At least 2 digits</li>
 * </ul>
 * This annotation can be applied to parameters and is processed by {@link ValidPasswordChecker}
 * @see ValidPasswordChecker
 */
@Documented
@Constraint(validatedBy = ValidPasswordChecker.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)

public @interface ValidPassword {
    String message() default "Lösenordet måste vara minst 8 tecken, innehålla 1 stor bokstav, 2 siffror och 2 specialtecken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
