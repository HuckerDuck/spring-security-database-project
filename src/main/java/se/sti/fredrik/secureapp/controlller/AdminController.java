package se.sti.fredrik.secureapp.controlller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import se.sti.fredrik.secureapp.DTO.AppUserDTO;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Service.AppUserService;

@RestController
public class AdminController {
    private final AppUserService userService;

    public AdminController(AppUserService userService) {
        this.userService = userService;
    }

    @Tag(name = "Admin Controller", description = "För hantering av användare")
    @PostMapping("/admin/register")
    @Operation  (summary = "Registrera en ny användare")
    public ResponseEntity<AppUser> register(@Valid @RequestBody AppUserDTO appUserDTO) {
        AppUser createdAppUser = userService.createUser(appUserDTO);
        return new ResponseEntity<>(createdAppUser, HttpStatus.CREATED);
    }

    @Tag(name = "Admin Controller", description = "För hantering av användare")
    @Operation  (summary = "Ta bort en användare")
    @DeleteMapping ("/admin/delete/{id}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Tag(name = "Admin Controller", description = "För admin översikt")
    @GetMapping("/admin")
    @ResponseBody
    public String getAdminInfo(Authentication authentication) {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        String username = jwt.getSubject();
        String roles = jwt.getClaimAsString("scope");

        return "Välkommen admin " + username + "! Du har roller: " + roles;
    }
}
