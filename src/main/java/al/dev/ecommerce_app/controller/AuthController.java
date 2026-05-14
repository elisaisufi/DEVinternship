package al.dev.ecommerce_app.controller;

import al.dev.ecommerce_app.dto.LoginDto;
import al.dev.ecommerce_app.dto.UserDto;
import al.dev.ecommerce_app.entity.User;
import al.dev.ecommerce_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/register-admin")
    public User registerAdmin(@Valid @RequestBody UserDto dto) {
        return userService.createAdmin(dto);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginDto dto) {
        return userService.login(dto);
    }

}
