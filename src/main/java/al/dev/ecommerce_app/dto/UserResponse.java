package al.dev.ecommerce_app.dto;

import al.dev.ecommerce_app.entity.User;
import al.dev.ecommerce_app.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    // converting entities into safe JSON ( we don't return password )
    public static UserResponse from(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
}