package se.sti.fredrik.secureapp.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.sti.fredrik.secureapp.Model.User;
import se.sti.fredrik.secureapp.Repository.UserRepository;

@Component
public class Bootstrap {

    private final UserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public Bootstrap(UserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (appUserRepository.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("ADMIN");
            appUserRepository.save(user);
        }
    }
}
