package al.dev.ecommerce_app.controller;

import al.dev.ecommerce_app.dto.LoginDto;
import al.dev.ecommerce_app.dto.UserDto;
import al.dev.ecommerce_app.dto.UserResponse;
import al.dev.ecommerce_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public UserResponse register(@Valid @RequestBody UserDto dto) {

        return userService.register(dto);
    }

    @PostMapping("/auth/login")
    public String login(@Valid @RequestBody LoginDto dto) {

        return userService.login(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/auth/register-admin")
    public UserResponse registerAdmin(@Valid @RequestBody UserDto dto) {

        return userService.createAdmin(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {

        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {

        userService.delete(id);
    }
}