package se.sti.fredrik.secureapp.Model;

import se.sti.fredrik.secureapp.exception.InvalidRoleException;

public enum Role {
    ADMIN, USER;

    public static boolean isValid(String value) {
        try {
            Role.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
