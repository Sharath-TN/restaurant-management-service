package com.restaurant.controller;

import com.restaurant.dto.LoginDTO;
import com.restaurant.dto.RegisterDTO;
import com.restaurant.service.AuthService;
import com.restaurant.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterDTO request) {
        log.debug("Received request to register admin: {}", request);
        authService.registerAdmin(request);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }
}
