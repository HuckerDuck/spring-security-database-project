package se.sti.fredrik.secureapp.controlller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sti.fredrik.secureapp.DTO.AppUserDTO;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Service.UserService;

@RestController
@RequestMapping("/userController")
public class UserAPIController {
    private final UserService userService;

    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    @Tag(name = "API Controller", description = "För hantering av användare")
    @PostMapping("/register")
    @Operation  (summary = "Registrera en ny användare")
    public ResponseEntity<AppUser> register(@Valid @RequestBody AppUserDTO appUserDTO) {
        AppUser createdAppUser = userService.createAppUser(appUserDTO);
        return new ResponseEntity<>(createdAppUser, HttpStatus.CREATED);
    }

    @Tag(name = "API Controller", description = "För hantering av användare")
    @Operation  (summary = "Ta bort en användare")
    @DeleteMapping ("/{id}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        userService.deleteAppUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
