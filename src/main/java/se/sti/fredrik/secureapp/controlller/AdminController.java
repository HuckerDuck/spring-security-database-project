package se.sti.fredrik.secureapp.controlller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import se.sti.fredrik.secureapp.DTO.AppUserDTO;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Service.AppUserService;

@Tag(name = "Admin Controller", description = "För hantering av användare")
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AppUserService appUserService;

    public AdminController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    @Operation  (summary = "Registrera en ny användare")
    public ResponseEntity<AppUser> register(@Valid @RequestBody AppUserDTO appUserDTO) {
        AppUser createdAppUser = appUserService.createAppUser(appUserDTO);
        return new ResponseEntity<>(createdAppUser, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation  (summary = "Ta bort en användare")
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        appUserService.deleteAppUser(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String getAdminInfo(Authentication authentication) {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        String username = jwt.getSubject();
        String roles = jwt.getClaimAsString("scope");

        return "Välkommen admin " + username + "! Du har roller: " + roles;
    }
}
