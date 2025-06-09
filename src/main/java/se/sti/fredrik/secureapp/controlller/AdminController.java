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
import se.sti.fredrik.secureapp.Config.RoutePaths;
import se.sti.fredrik.secureapp.DTO.UserDTO;
import se.sti.fredrik.secureapp.Model.User;
import se.sti.fredrik.secureapp.Service.AppUserService;

@RestController
@RequestMapping(RoutePaths.ADMIN_BASE)
public class AdminController {
    private final AppUserService userService;

    public AdminController(AppUserService userService) {
        this.userService = userService;
    }

    @Tag(name = "Admin Controller", description = "För hantering av användare")
    @PostMapping("/user/register")
    @Operation  (summary = "Registrera en ny användare")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO dto) {
        User createdAppUser = userService.createUser(dto);
        return new ResponseEntity<>(createdAppUser, HttpStatus.CREATED);
    }

    @Tag(name = "Admin Controller", description = "För hantering av användare")
    @Operation  (summary = "Ta bort en användare")
    @DeleteMapping ("/user/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Tag(name = "Admin Controller", description = "För admin översikt")
    @GetMapping("")
    @ResponseBody
    public String getAdminInfo(Authentication authentication) {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        String username = jwt.getSubject();
        String roles = jwt.getClaimAsString("scope");

        return "Välkommen admin " + username + "! Du har roller: " + roles;
    }
}
