package al.dev.ecommerce_app.service;

import al.dev.ecommerce_app.dto.LoginDto;
import al.dev.ecommerce_app.dto.UserDto;
import al.dev.ecommerce_app.entity.User;
import al.dev.ecommerce_app.enums.Role;
import al.dev.ecommerce_app.exception.CustomException;
import al.dev.ecommerce_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(UserDto dto) {

        if(userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new CustomException("Username already exists");
        }

        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public User login(LoginDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() ->
                        new CustomException("Invalid username or password")
                );

        if(!user.getPassword().equals(dto.getPassword())) {
            throw new CustomException("Invalid username or password");
        }

        return user;
    }

    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException("User not found")
                );
    }

    public User createAdmin(UserDto dto) {

        if(userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new CustomException("Username already exists");
        }

        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }

        User admin = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.ADMIN)
                .build();

        return userRepository.save(admin);
    }

    public List<User> getAllUsers() {
        return userRepository.findByIsActiveTrue();
    }
}