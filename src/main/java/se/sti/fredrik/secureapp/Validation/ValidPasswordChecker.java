package se.sti.fredrik.secureapp.Validation;
//? Denna är tänkt att meddela användaren om lösenordet de har valt inte följer mallen.
//? Alltså:
//? 1 stor bokstav, 2 siffror, 2 specialtecken.
//? Skickas sen för validation via DTO

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPasswordChecker implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context){
        //? Första kontrollen, stannar direkt om lösenordet är mindre än 8 tecken
        if (password == null || password.length() < 8 )
            return false;

        //? En varsin räknare för varje typ
        int upperCaseCount = 0;
        int digitcount = 0;
        int specialCount = 0;

        //? Denna for-loopen räknar och lägger till att
        for(char c : password.toCharArray()){
            if(Character.isUpperCase(c)){
                upperCaseCount++;
            }
            else if(Character.isDigit(c)){
                digitcount++;
            }
            else if (!Character.isLetterOrDigit(c)) {
            specialCount++;
            }
        }

        //? Denna kommer endast att returnera ett värde om alla mina 3 krav är uppfyllda
        //?  Stor Bokstav mer än 1,  Minst 2 siffor,    Minst 2 specialtecken
        return upperCaseCount >= 1 && digitcount >= 2 && specialCount >= 2;
    }
}
