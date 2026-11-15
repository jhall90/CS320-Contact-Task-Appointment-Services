package projectone.enhanced.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/*
 * The authenticated caller, pulled from the Spring Security section.
 */
public record CurrentUser(String username, boolean admin) {

    public static CurrentUser from(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
        return new CurrentUser(authentication.getName(), isAdmin);
    }

    /** Admins can reach every record; users only reach their own. */
    public boolean canAccess(OwnedEntity entity) {
        return admin || entity.getOwner().equals(username);
    }
}
