package projectone.enhanced.auth;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import projectone.enhanced.common.DuplicateResourceException;

@Repository
public class UserAccountRepository {

    private final ConcurrentMap<String, UserAccount> users = new ConcurrentHashMap<>();

    /* Usernames are compared case-insensitively so "JoshTheGreat" and "joshthegreat" cannot both exist. */
    private static String key(String username) {
        return username.toLowerCase(Locale.ROOT);
    }

    public UserAccount insert(UserAccount account) {
        if (users.putIfAbsent(key(account.username()), account) != null) {
            throw new DuplicateResourceException(account.username());
        }
        return account;
    }

    public Optional<UserAccount> findByUsername(String username) {
        return Optional.ofNullable(users.get(key(username)));
    }

    public List<UserAccount> findAll() {
        return List.copyOf(users.values());
    }

    public void clear() {
        users.clear();
    }
}
