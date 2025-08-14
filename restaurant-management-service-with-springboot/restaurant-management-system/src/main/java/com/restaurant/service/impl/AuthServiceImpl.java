package com.restaurant.service.impl;

import com.restaurant.dto.LoginDTO;
import com.restaurant.dto.RegisterDTO;
import com.restaurant.entity.User;
import com.restaurant.enumeration.Role;
import com.restaurant.exception.InvalidPasswordException;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.AuthService;
import com.restaurant.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void registerAdmin(RegisterDTO request) {
        log.debug("Received request to register admin: {}", request);
        if (userRepository.existsByRole(Role.ADMIN)) {
            log.error("An admin user already exists.");
            throw new IllegalStateException("An admin user already exists.");
        }
        if (request.getRole() != Role.ADMIN) {
            log.error("Only ADMIN role can be registered as an admin.");
            throw new IllegalStateException("Only ADMIN role can be registered as an admin.");
        }
        log.info("Registering admin with email: {}", request.getEmail());
        saveData(request);
    }

    @Override
    public void registerRestaurantEmployee(RegisterDTO request) {
        log.debug("Received request to register restaurant employee: {}", request);
        if (request.getRole()==Role.ADMIN) {
            log.error("Cannot register an admin as a restaurant employee.");
            throw new IllegalStateException("An admin user already exists.");
        }
        log.info("Registering restaurant employee with email: {}", request.getEmail());
        saveData(request);
    }

    @Override
    public String login(LoginDTO request) {
        log.debug("Received login request for email: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Invalid password for user: {}", request.getEmail());
            throw new RuntimeException("Invalid password");
        }
        log.info("User {} logged in successfully", user.getEmail());
        return jwtUtil.generateToken(user.getEmail());
    }

    private void validatePassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(regex)) {
            throw new InvalidPasswordException(
                    "Password does not meet the required criteria"
            );
        }
    }

    private void saveData(RegisterDTO request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        validatePassword(request.getPassword());
        userRepository.save(user);
    }
}
