package se.sti.fredrik.secureapp.Validation;

import se.sti.fredrik.secureapp.Model.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator class for the {@link ValidRole} annotation
 * <p>This checks whether a given role is valid:</p>
 * @see ValidRole
 */
public class ValidRoleChecker implements ConstraintValidator<ValidRole, String> {

    /**
     * Checks if the role input is valid by using the {@link Role} enum
     * @param value the inputted role to validate
     * @param context
     * @return True if the value is valid or false if not
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && Role.isValid(value.toUpperCase());
    }
}
