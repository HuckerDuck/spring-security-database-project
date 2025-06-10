package se.sti.fredrik.secureapp.Validation;

import se.sti.fredrik.secureapp.Model.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidRoleChecker implements ConstraintValidator<ValidRole, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && Role.isValid(value.toUpperCase());
    }
}
