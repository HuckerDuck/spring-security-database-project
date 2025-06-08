package se.sti.fredrik.secureapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.sti.fredrik.secureapp.Model.AppUser;

/**
 * Repository interface for accessing and managing {@link AppUser} entities
 * <p>
 * Extends {@link JpaRepository} to allow basic CRUD operations.
 * Includes a built-in method to find a user by username
 * </p>
 * @see AppUser
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Finds an {@link AppUser} by their username
     * @param username the username to find
     * @return the {@link AppUser} with the specified username
     */
    AppUser findByUsername(String username);
}
