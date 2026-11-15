package projectone.enhanced.auth;

import static projectone.enhanced.common.ValidationRules.PASSWORD_MAX;
import static projectone.enhanced.common.ValidationRules.PASSWORD_MIN;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import projectone.enhanced.security.SecurityProperties;

/*
 * Creates the ADMIN account at startup only when ADMIN_USERNAME and ADMIN_PASSWORD are provided. 
 * No built-in default admin login.
 */
@Component
public class AdminBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);

    private final AuthService authService;
    private final SecurityProperties props;

    public AdminBootstrap(AuthService authService, SecurityProperties props) {
        this.authService = authService;
        this.props = props;
    }

    @Override
    public void run(ApplicationArguments args) {
        String username = props.adminUsername();
        String password = props.adminPassword();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            log.info("No admin credentials configured; skipping admin account creation.");
            return;
        }
        if (password.length() < PASSWORD_MIN || password.length() > PASSWORD_MAX) {
            throw new IllegalStateException("ADMIN_PASSWORD must be "
                    + PASSWORD_MIN + "-" + PASSWORD_MAX + " characters");
        }
        authService.createAccount(username, password, Set.of(Role.ADMIN, Role.USER));
        log.info("Admin account '{}' created.", username);
    }
}
