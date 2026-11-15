package projectone.enhanced.auth;

import java.util.Set;

/* Stored user. Only the BCrypt hash is kept, never the plain password. */
public record UserAccount(String username, String passwordHash, Set<Role> roles) {

    public UserAccount {
        roles = Set.copyOf(roles);
    }
}
