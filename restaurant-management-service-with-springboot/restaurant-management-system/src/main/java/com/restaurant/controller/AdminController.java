package com.restaurant.controller;

import com.restaurant.dto.BillResponseDTO;
import com.restaurant.dto.RegisterDTO;
import com.restaurant.service.AuthService;
import com.restaurant.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AuthService authService;

    private final OrderService orderService;

    public AdminController(AuthService authService, OrderService orderService) {
        this.authService = authService;
        this.orderService = orderService;
    }

    @PostMapping("/registerRestaurantEmployee")
    public ResponseEntity<String> registerRestaurantEmployee(@Valid @RequestBody RegisterDTO request) {
        log.debug("Received request to register restaurant employee: {}", request);
        authService.registerRestaurantEmployee(request);
        return ResponseEntity.ok("Restaurant employee registered successfully");
    }

    @PostMapping("/generateBill/{orderId}")
    public ResponseEntity<BillResponseDTO> generateBill(@PathVariable Long orderId) {
        log.debug("Received request to generate bill for order ID: {}", orderId);
        return ResponseEntity.ok(orderService.generateBill(orderId));
    }
}
