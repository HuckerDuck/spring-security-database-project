package se.sti.fredrik.secureapp.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String role) {
        super("The role '" + role + "' is not recognized as a valid system role.");
    }
}
