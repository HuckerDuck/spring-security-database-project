package se.sti.fredrik.secureapp.controlller;

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

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@Valid @RequestBody AppUserDTO appUserDTO) {
        AppUser createdAppUser = userService.createAppUser(appUserDTO);
        return new ResponseEntity<>(createdAppUser, HttpStatus.CREATED);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        userService.deleteAppUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
