package projectone.enhanced.auth;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projectone.enhanced.auth.AuthDtos.UserResponse;

/* Only reachable with ROLE_ADMIN. SecurityConfig makes this happen */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserAccountRepository users;

    public AdminController(UserAccountRepository users) {
        this.users = users;
    }

    @GetMapping("/users")
    public List<UserResponse> listUsers() {
        return users.findAll().stream().map(UserResponse::from).toList();
    }
}
