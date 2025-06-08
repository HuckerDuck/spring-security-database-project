package se.sti.fredrik.secureapp.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;

/**
 * {@code Bootstrap} is a Spring {@link org.springframework.stereotype.Component} that initializes
 * the application with default users for testing purposes
 * <p>
 * It uses {@link jakarta.annotation.PostConstruct} to automatically create an "admin" and a "user"
 * account after the application context is initialized, if they do not already exist
 */
@Component
public class Bootstrap {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public Bootstrap(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * <p>Initializes default users ("admin" | "user") with pre-defined credentials if not already present in the database</p>
     * <p>This runs automatically after Spring completes dependency injection</p>
     */
    @PostConstruct
    public void init() {
        if (appUserRepository.findByUsername("admin") == null) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole("ADMIN");
            admin.setGivenConsent(true);
            appUserRepository.save(admin);
        }

        if (appUserRepository.findByUsername("user") == null) {
            AppUser user = new AppUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("USER");
            user.setGivenConsent(true);
            appUserRepository.save(user);
        }
    }
}
