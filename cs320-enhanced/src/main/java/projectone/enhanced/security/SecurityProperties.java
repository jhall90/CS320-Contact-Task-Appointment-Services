package projectone.enhanced.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * Values bound from application.properties which are read them from environment variables. 
 * Nothing secret is stored in the source code.
 */
@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
        String jwtSecret,
        long tokenTtlMinutes,
        String adminUsername,
        String adminPassword) {
}
