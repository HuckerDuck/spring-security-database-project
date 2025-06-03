package se.sti.fredrik.secureapp.Model;

//? Skapar en användare som har Usernamn i String
//? Password i String
//? En role i String för att ange vilket role som denna användare ha
//? En Boolean som är sann eller falsk när användaren ger samtycke längre fram.


public class AppUser {
    private String username;
    private String password;
    private String role;
    private Boolean givenConsent;

    public AppUser(String username, String password, String role, Boolean givenConcent) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.givenConsent = givenConcent;
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

    public Boolean getGivenConcent() {
        return givenConcent;
    }

    public void setGivenConcent(Boolean givenConcent) {
        this.givenConcent = givenConcent;
    }
}
