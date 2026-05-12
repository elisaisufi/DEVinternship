package al.dev.ecommerce_app.Controller;

import al.dev.ecommerce_app.Dto.LoginDto;
import al.dev.ecommerce_app.Dto.UserDto;
import al.dev.ecommerce_app.Entity.User;
import al.dev.ecommerce_app.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserDto dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginDto dto) {
        return userService.login(dto);
    }
}
