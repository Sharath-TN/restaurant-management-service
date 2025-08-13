package com.restaurant.service;

import com.restaurant.dto.LoginDTO;
import com.restaurant.dto.RegisterDTO;
import com.restaurant.entity.User;
import com.restaurant.enumeration.Role;
import com.restaurant.exception.InvalidPasswordException;
import com.restaurant.repository.UserRepository;
import com.restaurant.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;


    public void registerAdmin(RegisterDTO request) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new IllegalStateException("An admin user already exists.");
        }
        if (request.getRole() != Role.ADMIN) {
            throw new IllegalStateException("Only ADMIN role can be registered as an admin.");
        }
        saveData(request);
    }

    public void registerRestaurantEmployee(RegisterDTO request) {
        if (request.getRole()==Role.ADMIN) {
            throw new IllegalStateException("An admin user already exists.");
        }
        saveData(request);
    }

    public String login(LoginDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

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
