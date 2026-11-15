package projectone.enhanced.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import projectone.enhanced.auth.AuthDtos.TokenResponse;
import projectone.enhanced.security.JwtConfig;
import projectone.enhanced.security.SecurityProperties;

@Service
public class AuthService {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final Duration tokenTtl;
    // Compared against when the username does not exist, so a failed login takes about the same time either way and does not reveal valid usernames.
    private final String dummyHash;

    public AuthService(UserAccountRepository users, PasswordEncoder passwordEncoder,
                       JwtEncoder jwtEncoder, SecurityProperties props) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.tokenTtl = Duration.ofMinutes(props.tokenTtlMinutes());
        this.dummyHash = passwordEncoder.encode("timing-equalizer-not-a-real-password");
    }

    /* Public registration always creates a USER. Admins come from config only. */
    public UserAccount register(String username, String rawPassword) {
        return createAccount(username, rawPassword, Set.of(Role.USER));
    }

    UserAccount createAccount(String username, String rawPassword, Set<Role> roles) {
        return users.insert(new UserAccount(username, passwordEncoder.encode(rawPassword), roles));
    }

    public TokenResponse login(String username, String rawPassword) {
        UserAccount account = users.findByUsername(username).orElse(null);
        String hash = account != null ? account.passwordHash() : dummyHash;
        boolean matches = passwordEncoder.matches(rawPassword, hash);
        if (account == null || !matches) {
            throw new InvalidCredentialsException();
        }
        return issueToken(account);
    }

    private TokenResponse issueToken(UserAccount account) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(JwtConfig.ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(tokenTtl))
                .subject(account.username())
                .claim("roles", account.roles().stream().map(Role::name).toList())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new TokenResponse(token, "Bearer", tokenTtl.toSeconds());
    }
}
