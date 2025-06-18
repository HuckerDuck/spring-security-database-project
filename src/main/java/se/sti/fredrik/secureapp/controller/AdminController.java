package se.sti.fredrik.secureapp.controller;

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
import se.sti.fredrik.secureapp.DTO.SetUserRoleDTO;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Service.AppUserService;

/**
 * Denna admincontroller är till för att hanterra anvädare som en admin.
 * Med denna kan du lägga till användare, ta bort användare och sätter roller för olika användare och admins
 * Endpointsen i denna kräver att man authentiserar sig som admin för en ökad säkerhet
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AppUserService appUserService;

    /**
     * Konstruktur som används för att kunna använda appUserService i denna klass
     * @param appUserService denna klass har all logik i sig och behövs skickas och användas i denna klass
     */
    public AdminController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     *
     * @param appUserDTO använder ett DTo som innehåller användardata (användarnamn, lösenord och godkännande av samtycke
     * @return ResponceEntity med en skapad appAnvändare och med ett HTTP status av att den är skapad
     */
    @Tag(name = "Admin Controller", description = "För hantering av användare")
    @PostMapping("/register")
    @Operation  (summary = "Registrera en ny användare")
    public ResponseEntity<AppUser> register(@Valid @RequestBody AppUserDTO appUserDTO) {
        AppUser createdAppUser = appUserService.createUser(appUserDTO);
        return new ResponseEntity<>(createdAppUser, HttpStatus.CREATED);
    }

    /**
     *
     * @param id användaren unika id
     * @return ResponeEntity med en HTTP status av No_Content om den lyckades ta bort en användare
     */
    @Tag(name = "Admin Controller", description = "För hantering av användare")
    @Operation  (summary = "Ta bort en användare")
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     *
     * @param authentication som innehåller JWT token som används till authentiseringen
     * @return Sträng med användarnamn och roller och text till detta.
     */
    @Tag(name = "Admin Controller", description = "För admin översikt")
    @GetMapping()
    @ResponseBody
    public String getAdminInfo(Authentication authentication) {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        String username = jwt.getSubject();
        String roles = jwt.getClaimAsString("scope");

        return "Välkommen admin " + username + "! Du har roller: " + roles;
    }


    /**
     *
     * @param dto DTo som innehåller användar-ID som är unikt och sätter en roll
     *            man t.ex. kan göra en användare till en admin
     * @return ResponceEntity med en uppdaterade användare och HttpStatus av att allt har gått bra.
     */
    @Tag(name = "Admin Controller", description = "För admin översikt")
    @PutMapping("/setrole")
    @Operation(summary = "Sätt en ny roll för en användare")
    public ResponseEntity<AppUser> setUserRole(@RequestBody @Valid SetUserRoleDTO dto) {
        AppUser updatedUser = appUserService.setRole(dto.getId(), dto.getRole());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
