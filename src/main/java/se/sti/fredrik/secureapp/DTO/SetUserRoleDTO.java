package se.sti.fredrik.secureapp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SetUserRoleDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String role;

    public SetUserRoleDTO(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
