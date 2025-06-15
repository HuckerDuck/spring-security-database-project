package se.sti.fredrik.secureapp.Model;



/**
 * A standard enum of roles a user can have
 */
public enum Role {
    ADMIN, USER;

    /**
     * Check if a role can match to enum
     * @param value the role to match
     * @return a true if a match exists, false otherwise
     */
    public static boolean isValid(String value) {
        try {
            Role.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
