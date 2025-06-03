package se.sti.fredrik.secureapp.controlller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("request-token")
public class AuthController {

    @PostMapping
    public ResponseEntity<String> token(@RequestBody LoginRequest loginRequest) {
        Respons
    }
}
