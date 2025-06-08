package se.sti.fredrik.secureapp.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents an application user with credentials
 * <p>
 * This {@code @Entity} is persisted in the database and includes fields for:
 * * username, password, role, and a consent flag
 * </p>
 */
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    private Boolean givenConsent;

    /**
     * Empty constructor to allow Spring-boot to initialize:
     * <ul>
     *     <li>Unique identifier (primary key | Id)</li>
     *     <li>Username</li>
     *     <li>Encrypted user password</li>
     *     <li>User role</li>
     *     <li>Flag indicating whether user has granted consent</li>
     * </ul>
     */
    public AppUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getGivenConsent() {
        return givenConsent;
    }

    public void setGivenConsent(Boolean givenConsent) {
        this.givenConsent = givenConsent;
    }

    public Long getId() {
        return id;

    }
}
