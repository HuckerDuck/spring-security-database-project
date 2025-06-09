package se.sti.fredrik.secureapp.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sti.fredrik.secureapp.DTO.AppUserDTO;

import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;
import se.sti.fredrik.secureapp.component.LoggerComponent;
import se.sti.fredrik.secureapp.exception.UserNotFoundException;

import java.util.Optional;
//**
//* Serviceklass för att hantera AppUser-entitet
//* Denna ServiceKlass används för att skapa och ta bort användare
//* Sen sker Logging via en LoggingComponent
//*
@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoggerComponent loggerComponent;


    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, LoggerComponent loggerComponent) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.loggerComponent = loggerComponent;
    }

    /**
     *
     * @param appUserDTO, vilket är ett dataöverföringsobjekt
     *                    kräver användare, lösenord och ett samtycke
     *
     * @return returnerar den skapade användaren med ett eget id
     * @throws RuntimeException Kastar RuntimeException om användaren redan finns i databasen
     * @throws IllegalArgumentException Kastar IllegalArgumentException om samtycke ej är inlagt
     * @implNote Använder sedan LoggerComponent för att logga händelsen till en fil
     */

    //? Metod för att skapa en ny användare
    public AppUser createAppUser(AppUserDTO appUserDTO) {

        AppUser appUser = appUserRepository.findByUsername(appUserDTO.getUsername());
        if (appUser != null) {
            throw new RuntimeException("Username already exists");
        }

        if(appUserDTO.getGivenConsent()==null || !appUserDTO.getGivenConsent()){
            throw new IllegalArgumentException("You have to give concent to register");
        }

        AppUser newAppUser = new AppUser();

        newAppUser.setUsername(appUserDTO.getUsername());
        newAppUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        newAppUser.setRole(appUserDTO.getRole());
        newAppUser.setGivenConsent(appUserDTO.getGivenConsent());

        AppUser savedAppUser = appUserRepository.save(newAppUser);
        loggerComponent.loggingForLogin(savedAppUser.getUsername());

        return savedAppUser;
    }

    /**
     *
     * @param appUserId använder appUserID:t för att hitta användaren i databasen
     *                  Tar bort den om den hittar det
     * @throws UserNotFoundException kastar UserNotFoundException om den inte hittar användaren i systemet
     *
     */
    public void deleteAppUser(Long appUserId) {

        Optional<AppUser> appUser = appUserRepository.findById(appUserId);
        if (appUser.isPresent())
        {
            loggerComponent.loggingDeletionOfUser(appUser.get().getUsername());
            appUserRepository.delete(appUser.get());
        }

        else
        {
            throw new UserNotFoundException("The app user does not exist " + appUserId);
        }
    }


}
