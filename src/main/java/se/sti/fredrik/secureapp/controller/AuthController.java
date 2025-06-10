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

@RestController
@RequestMapping(RoutePaths.AUTH_BASE)
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

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
