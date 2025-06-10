package se.sti.fredrik.secureapp.exception;

public class AdminDemoteNotAllowedException extends RuntimeException {
    public AdminDemoteNotAllowedException() {
        super("Demoting an admin is not allowed.");
    }
}
