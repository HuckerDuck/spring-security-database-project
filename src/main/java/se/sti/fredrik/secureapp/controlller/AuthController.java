package se.sti.fredrik.secureapp.controlller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request-token")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private se.sti.fredrik.secureapp.service.TokenService tokenService;
    public record LoginRequest(String username, String password) {}
    @PostMapping
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
