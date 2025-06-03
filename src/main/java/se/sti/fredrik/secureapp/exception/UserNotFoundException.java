package se.sti.fredrik.secureapp.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super("The user " + message + " was not found");
    }
}
