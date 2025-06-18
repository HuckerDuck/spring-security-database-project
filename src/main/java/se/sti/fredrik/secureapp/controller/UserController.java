package se.sti.fredrik.secureapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Denna controller är för användare som har loggat in
 * Den returnerar ett enkelt text med "Välkommen user!" när användaren har kommit in
 */

@RestController
public class UserController {

    /**
     * Returnerar ett välkomstmeddelande när användaren loggar in
     * @return text med string som är "Välkommen user!"
     */
    @Tag(name = "User Controller", description = "För användaren")
    @GetMapping("/user")
    @ResponseBody
    public String getUser() {
        return "Välkommen user!";
    }
}