package se.sti.fredrik.secureapp.exception;

public class InsufficientPrivilegesException extends RuntimeException {
    public InsufficientPrivilegesException() {
        super("You do not have permission to modify this user.");
    }
}

