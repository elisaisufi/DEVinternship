package al.dev.ecommerce_app.service;

import al.dev.ecommerce_app.dto.LoginDto;
import al.dev.ecommerce_app.dto.UserDto;
import al.dev.ecommerce_app.dto.UserResponse;
import al.dev.ecommerce_app.entity.User;
import al.dev.ecommerce_app.enums.Role;
import al.dev.ecommerce_app.exception.CustomException;
import al.dev.ecommerce_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserResponse register(UserDto dto) {

        validateUser(dto);

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .isActive(true)
                .build();

        return UserResponse.from(userRepository.save(user));
    }

    public String login(LoginDto dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        return "Login successful";
    }

    public UserResponse createAdmin(UserDto dto) {

        validateUser(dto);

        User admin = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.ADMIN)
                .isActive(true)
                .build();

        return UserResponse.from(userRepository.save(admin));
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findByIsActiveTrue()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    public void delete(Long id) {

        User user = getById(id);

        user.setActive(false);

        userRepository.save(user);
    }

    public User getByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new CustomException("User not found")
                );

        if (!user.isActive()) {
            throw new CustomException("User not found");
        }

        return user;
    }

    public User getById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException("User not found")
                );

        if (!user.isActive()) {
            throw new CustomException("User not found");
        }

        return user;
    }

    private void validateUser(UserDto dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new CustomException("Username already exists");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }
    }
}