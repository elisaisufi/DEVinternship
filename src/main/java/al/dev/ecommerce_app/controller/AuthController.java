package al.dev.ecommerce_app.controller;

import al.dev.ecommerce_app.dto.LoginDto;
import al.dev.ecommerce_app.dto.UserDto;
import al.dev.ecommerce_app.entity.User;
import al.dev.ecommerce_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserDto dto) {

        return userService.register(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register-admin")
    public User registerAdmin(@Valid @RequestBody UserDto dto) {

        return userService.createAdmin(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginDto dto) {

        return userService.login(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {

        userService.delete(id);
    }
}
