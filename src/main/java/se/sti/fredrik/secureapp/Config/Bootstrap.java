package se.sti.fredrik.secureapp.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;

@Component
public class Bootstrap {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public Bootstrap(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metod för att snabbt kunna konstruera två users [admin | user] --
    // för att kunna testa funktioner och hantera första inloggningen
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
