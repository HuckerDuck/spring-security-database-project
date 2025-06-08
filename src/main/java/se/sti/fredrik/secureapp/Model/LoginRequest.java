package se.sti.fredrik.secureapp.Model;

/**
 * Represents a login request containing the username and password
 * <p>This record is used in authentication endpoints</p>
 *
 * @param username the username of the user attempting to log in
 * @param password the password of the user attempting to log in
 */
public record LoginRequest(String username, String password) {
}
