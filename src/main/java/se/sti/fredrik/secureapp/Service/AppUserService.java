package se.sti.fredrik.secureapp.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sti.fredrik.secureapp.DTO.AppUserDTO;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;
import se.sti.fredrik.secureapp.component.LoggerComponent;
import se.sti.fredrik.secureapp.exception.AdminDemoteNotAllowedException;
import se.sti.fredrik.secureapp.exception.RoleAlreadyAssignedException;
import se.sti.fredrik.secureapp.exception.UserNotFoundException;
import se.sti.fredrik.secureapp.exception.UsernameAlreadyExistsException;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;
//**
//* Serviceklass för att hantera AppUser-entitet
//* Denna ServiceKlass används för att skapa och ta bort användare
//* Sen sker Logging via en LoggingComponent
//*
@Service
public class AppUserService {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoggerComponent logger;

    public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder, LoggerComponent logger) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger;
    }

    /**
     *
     * @param dto vilket är ett dataöverföringsobjekt
     *                    kräver användare, lösenord och ett samtycke
     *
     * @return returnerar den skapade användaren med ett eget id
     * @throws RuntimeException Kastar RuntimeException om användaren redan finns i databasen
     * @throws IllegalArgumentException Kastar IllegalArgumentException om samtycke ej är inlagt
     * @implNote Använder sedan LoggerComponent för att logga händelsen till en fil
     */
    public AppUser createUser(AppUserDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new UsernameAlreadyExistsException(dto.getUsername());
        }

        AppUser newAppUser = new AppUser();
        newAppUser.setUsername(dto.getUsername());
        newAppUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newAppUser.setRole(dto.getRole().toUpperCase());
        newAppUser.setGivenConsent(dto.getGivenConsent());

        AppUser savedAppUser = userRepository.save(newAppUser);
        logger.loggAddedUser(savedAppUser.getUsername());

        return savedAppUser;
    }

    /**
     *
     * @param id använder appUserID:t för att hitta användaren i databasen
     *                  Tar bort den om den hittar det
     * @throws UserNotFoundException kastar UserNotFoundException om den inte hittar användaren i systemet
     *
     */
    public void deleteUser(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
        logger.loggingForLogin("Deleted user: " + id);
    }



    /**
     * Sätter en ny roll för en användare med angivet ID.
     *
     * @param id       Användarens ID
     * @param newRole  Ny roll (t.ex. "USER", "ADMIN")
     * @return Uppdaterad användare
     *
     * @throws UserNotFoundException           Om användaren inte hittas
     * @throws RoleAlreadyAssignedException    Om användaren redan har rollen
     * @throws AdminDemoteNotAllowedException  Om en admin försöker nedgraderas till user
     */
    public AppUser setRole(Long id, String newRole) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.getRole().equals(newRole)) {
            throw new RoleAlreadyAssignedException(newRole);
        }

        if (user.getRole().equals("ADMIN") && newRole.equals("User")) {
            throw new AdminDemoteNotAllowedException();
        }

        user.setRole(newRole);
        return userRepository.save(user);
    }

}
