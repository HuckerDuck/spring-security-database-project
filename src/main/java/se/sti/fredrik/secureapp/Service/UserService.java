package se.sti.fredrik.secureapp.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sti.fredrik.secureapp.DTO.UserDTO;
import se.sti.fredrik.secureapp.Model.User;
import se.sti.fredrik.secureapp.Repository.UserRepository;
import se.sti.fredrik.secureapp.component.LoggerComponent;
import se.sti.fredrik.secureapp.exception.UserNotFoundException;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoggerComponent loggerComponent;

    public UserService(UserRepository appUserRepository, PasswordEncoder passwordEncoder, LoggerComponent loggerComponent) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.loggerComponent = loggerComponent;
    }

    //? Metod för att skapa en ny användare
    public User createAppUser(UserDTO appUserDTO) {
        //? Kolla om användaren redan finns:
        User appUser = appUserRepository.findByUsername(appUserDTO.getUsername());
        if (appUser != null) {
            throw new RuntimeException("Username already exists");
        }

        //? Skapar en ny användare
        User newAppUser = new User();

        //? Använder Setter via DTO för att sätta användarnamn, lösenord, roll och att den har givit sitt samtycke
        newAppUser.setUsername(appUserDTO.getUsername());
        newAppUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        newAppUser.setRole(appUserDTO.getRole());
        newAppUser.setGivenConsent(appUserDTO.getGivenConsent());

        User savedAppUser = appUserRepository.save(newAppUser);
        loggerComponent.loggingForLogin(savedAppUser.getUsername());

        return savedAppUser;
    }

    //? Tar bort en användare
    public void deleteAppUser(Long appUserId) {
        //? Optional <T> är en container som innehåller ett värde av T
        //? Optional kan vara tom.
        //? Anledningen till att använda Optional och inte null är för att null kan
        //? Leda till buggar med NullPointerException
        //? Optional har extra metoder som isPresent(), isEmpty(), get(), orElseThrow()

        Optional<User> appUser = appUserRepository.findById(appUserId);
        if (appUser.isPresent()) {
            appUserRepository.delete(appUser.get());
        }
        else{
            throw new UserNotFoundException("The app user does not exist " + appUserId);
        }
    }


}
