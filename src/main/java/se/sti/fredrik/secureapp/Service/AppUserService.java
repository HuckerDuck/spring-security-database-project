package se.sti.fredrik.secureapp.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sti.fredrik.secureapp.DTO.UserDTO;
import se.sti.fredrik.secureapp.Model.User;
import se.sti.fredrik.secureapp.Repository.UserRepository;
import se.sti.fredrik.secureapp.component.LoggerComponent;
import se.sti.fredrik.secureapp.exception.UserNotFoundException;
import se.sti.fredrik.secureapp.exception.UsernameAlreadyExistsException;

@Service
public class AppUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoggerComponent logger;

    public AppUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LoggerComponent logger) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger;
    }

    /**
     * Creates and saves a new user based on the provided user data.
     *
     * @param dto the data transfer object containing user registration details
     * @return the saved User entity
     * @throws UsernameAlreadyExistsException if the username already exists in the system
     */
    public User createUser(UserDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new UsernameAlreadyExistsException(dto.getUsername());
        }

        User newAppUser = new User();
        newAppUser.setUsername(dto.getUsername());
        newAppUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newAppUser.setRole(dto.getRole());
        newAppUser.setGivenConsent(dto.getGivenConsent());

        User savedAppUser = userRepository.save(newAppUser);
        logger.loggingForLogin(savedAppUser.getUsername());

        return savedAppUser;
    }

    
    /**
     * Deletes a user from the system by their unique id.
     *
     * @param id the unique identifier of the user to be deleted.
     *           If the user does not exist, a {@link UserNotFoundException} is thrown.
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
        logger.loggingForLogin("Deleted user: " + id);
    }



}
