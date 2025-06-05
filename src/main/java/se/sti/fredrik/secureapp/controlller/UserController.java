package se.sti.fredrik.secureapp.controlller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Tag(name = "User Controller", description = "För användaren")
    @GetMapping("/user")
    @ResponseBody
    public String getUser() {
        return "Välkommen user!";
    }
}