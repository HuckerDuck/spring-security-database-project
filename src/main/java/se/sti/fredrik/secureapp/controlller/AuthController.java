package se.sti.fredrik.secureapp.controlller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.sti.fredrik.secureapp.Model.LoginRequest;
import se.sti.fredrik.secureapp.Service.TokenService;
import se.sti.fredrik.secureapp.exception.UserTestingException;

@RestController
@RequestMapping("/request-token")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    @PostMapping
    public ResponseEntity<String> token(@RequestBody LoginRequest loginRequest) {
        try {


            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            String token = tokenService.generateToken(auth);
            return ResponseEntity.ok(token);
        } catch (UserTestingException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
