package se.sti.fredrik.secureapp.Model;

//? Skapar en användare som har Usernamn i String
//? Password i String
//? En role i String för att ange vilket role som denna användare ha
//? En Boolean som är sann eller falsk när användaren ger samtycke längre fram.

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

public class AppUser {
    private String username;
    private String password;
    private String role;
    private Boolean givenConsent;

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

    public Boolean getGivenConsent() {
        return givenConsent;
    }

    public void setGivenConsent(Boolean givenConsent) {
        this.givenConsent = givenConsent;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
