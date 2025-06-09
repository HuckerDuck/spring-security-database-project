package se.sti.fredrik.secureapp.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;

/**
 * {@code LoggerComponent} is a simple logging utility for logging for user-related actions such as
 * registration, login and deletion
 * <p>Logs to a .log to maintain consistent log output</p>
 */
@Component
public class LoggerComponent {
    private final Logger logger = LoggerFactory.getLogger(LoggerComponent.class);

    public LoggerComponent() {
    }

    /**
     * Logs an informational message when a user logs in
     * @param username the username of the user who logged in
     */
    public void loggingForLogin(String username) {
        logger.warn("A user logged in {}", username);
    }

    /**
     * Logs an informational message when a user is deleted
     * @param username the username of the deleted user
     */
    public void loggingDeletionOfUser(String username) {
        logger.warn("A user was deleted: {}", username);
    }

    public void loggingForLogout(String username) {
        logger.warn("A user logged out: {}", username);
    }

    public void loggAddedUser(String username) {
        logger.warn("A user was added with username: " + username);

    }
}
