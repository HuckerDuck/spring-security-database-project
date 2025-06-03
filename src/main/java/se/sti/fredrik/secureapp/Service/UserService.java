package se.sti.fredrik.secureapp.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;

@Service
public class UserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoggingComponent loggingComponent;

    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, LoggingComponent loggingComponent) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.loggingComponent = loggingComponent;
    }


}
