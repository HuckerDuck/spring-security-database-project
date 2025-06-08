package se.sti.fredrik.secureapp.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator class for the {@link ValidPassword} annotation
 * <p>This checks whether a given password follows the rules:</p>
 * <ul>
 *     <li>Minimum length of 8 characters</li>
 *     <li>At least 1 uppercase letter</li>
 *     <li>At least 2 special characters</li>
 *     <li>At least 2 digits</li>
 * </ul>
 * @see ValidPassword
 */
public class ValidPasswordChecker implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    /**
     * Validates the password against the constraints
     *
     * @param password the password to validate
     * @param context
     * @return {@code true} if the password is valid, {@code false} otherwise
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        // Check for 8 characters
        if (password == null || password.length() < 8)
            return false;

        // Counts for all criteria
        int upperCaseCount = 0;
        int digitcount = 0;
        int specialCount = 0;

        //? For-loop to verify entire String against counts
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upperCaseCount++;
            } else if (Character.isDigit(c)) {
                digitcount++;
            } else if (!Character.isLetterOrDigit(c)) {
                specialCount++;
            }
        }

        return upperCaseCount >= 1 && digitcount >= 2 && specialCount >= 2;
    }
}
