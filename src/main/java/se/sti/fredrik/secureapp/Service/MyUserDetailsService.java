package se.sti.fredrik.secureapp.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;


import java.util.List;

/**
 * {@code MyUserDetailsService} is a custom implementation of {@link UserDetailsService}.
 * <p>
 * It loads user-specific data from the database using {@link AppUserRepository}
 * and grants Spring Security authorities based on the user's role.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public MyUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Loads the user from the database by username and converts it into a Spring Security {@link UserDetails} object.
     * @param username the username of the user whose data is required
     * @return the {@link UserDetails} of the authenticated user
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
