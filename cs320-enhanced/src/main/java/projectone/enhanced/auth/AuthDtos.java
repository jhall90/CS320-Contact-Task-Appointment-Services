package projectone.enhanced.auth;

import static projectone.enhanced.common.ValidationRules.PASSWORD_MAX;
import static projectone.enhanced.common.ValidationRules.PASSWORD_MIN;
import static projectone.enhanced.common.ValidationRules.USERNAME_PATTERN;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record RegisterRequest(
            @NotBlank @Pattern(regexp = USERNAME_PATTERN,
                    message = "must be 3-20 letters, numbers, or underscores") String username,
            @NotBlank @Size(min = PASSWORD_MIN, max = PASSWORD_MAX) String password) {
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record TokenResponse(String accessToken, String tokenType, long expiresInSeconds) {
    }

    public record UserResponse(String username, Set<Role> roles) {

        static UserResponse from(UserAccount a) {
            return new UserResponse(a.username(), a.roles());
        }
    }
}
