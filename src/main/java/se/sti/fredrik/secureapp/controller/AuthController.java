package se.sti.fredrik.secureapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.sti.fredrik.secureapp.Config.RoutePaths;
import se.sti.fredrik.secureapp.Model.LoginRequest;
import se.sti.fredrik.secureapp.Service.TokenService;

/**
 * Denna kontroller är till för att sköta authentiseringen och för inloggning i sig
 * Här kan en framtida användare skicka in sina inloggningsuppgifter och sen få ett JWT-token tillbaka
 */
@RestController
@RequestMapping(RoutePaths.AUTH_BASE)
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    /**
     * Konstruktor för att kunna använda authentiseringen och skapande av token i denna klass
     *
     * @param authenticationManager amsvarar för att kolla att uppgifterna som kommer in stämmer
     * @param tokenService ansvarar för att sedan generera ett giltligt JWT-token som används
     *                     för authentiseringen
     */


    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    /**
     *
     * @param loginRequest denna innehåller användarnamn och lösenord
     * @return ResponceEntity med ett JWT-token och en HTTP kod om allt går som det ska
     */
    @Tag(name = "Auth Controller", description = "Authentisering och hantering av inloggning")
    @PostMapping("/request-token")
    public ResponseEntity<String> token(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        String token = tokenService.generateToken(auth);
        return ResponseEntity.ok(token);
    }

}
