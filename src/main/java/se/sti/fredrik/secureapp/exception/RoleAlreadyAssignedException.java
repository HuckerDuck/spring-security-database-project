package se.sti.fredrik.secureapp.exception;

public class RoleAlreadyAssignedException extends RuntimeException {
    public RoleAlreadyAssignedException(String role) {
        super("User already has role '" + role + "'. No update necessary.");
    }
}
