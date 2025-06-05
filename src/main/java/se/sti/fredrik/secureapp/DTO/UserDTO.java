package se.sti.fredrik.secureapp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import se.sti.fredrik.secureapp.Validation.ValidPassword;

public class UserDTO {
    @NotBlank
    private String username;

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String role;

    @NotNull
    private Boolean GivenConsent;

    //?                         Getter & Setter

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public Boolean getGivenConsent() {return GivenConsent;}
    public void setGivenConsent(Boolean givenConsent) {GivenConsent = givenConsent;}
}
