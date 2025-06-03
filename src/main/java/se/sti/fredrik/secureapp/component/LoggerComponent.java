package se.sti.fredrik.secureapp.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//? Denna klass är tänkt att användas för att logga information.
//? Loggningen kommer att ske vid inloggning och utloggning

@Component
public class LoggerComponent {
    private final Logger logger = LoggerFactory.getLogger(LoggerComponent.class);

    //? Skriver ut när en användare loggas in
    public void loggingForLogin(String username) {
        logger.info("A user logged in " + username);

    }

    //? Skriver ut när en användare tas bort
    public void loggingDeletionofUser(String username) {
        logger.info("A user was deleted: " + username);
    }
}
