package se.sti.fredrik.secureapp.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.sti.fredrik.secureapp.Model.User;
import se.sti.fredrik.secureapp.Repository.UserRepository;
import se.sti.fredrik.secureapp.exception.UserTestingException;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository appUserRepository;

    public MyUserDetailsService(UserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new UserTestingException("Användaren med id " + appUser.getId() + "hittades inte");
        }

        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                true, true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getRole()))
        );
    }
}
