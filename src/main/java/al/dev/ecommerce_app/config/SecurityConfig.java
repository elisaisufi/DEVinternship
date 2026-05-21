package al.dev.ecommerce_app.config;

import al.dev.ecommerce_app.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/auth/register",
                                "/auth/login"
                        ).permitAll()

                        .requestMatchers(
                                "/auth/register-admin"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/products/**",
                                "/categories/**"
                        ).hasAnyRole("ADMIN", "USER")

                        .requestMatchers(
                                "/cart/**",
                                "/orders/**"
                        ).hasRole("USER")

                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}