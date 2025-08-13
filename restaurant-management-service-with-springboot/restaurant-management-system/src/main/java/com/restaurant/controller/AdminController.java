package com.restaurant.controller;

import com.restaurant.dto.BillResponseDTO;
import com.restaurant.dto.RegisterDTO;
import com.restaurant.service.AuthService;
import com.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AuthService authService;

    private final OrderService orderService;

    public AdminController(AuthService authService, OrderService orderService) {
        this.authService = authService;
        this.orderService = orderService;
    }

    @PostMapping("/registerRestaurantEmployee")
    public ResponseEntity<String> registerRestaurantEmployee(@RequestBody RegisterDTO request) {

        authService.registerRestaurantEmployee(request);
        return ResponseEntity.ok("Restaurant employee registered successfully");
    }

    @PostMapping("/generateBill/{orderId}")
    public ResponseEntity<BillResponseDTO> generateBill(@PathVariable Long orderId) {
         return ResponseEntity.ok(orderService.generateBill(orderId));
    }
}
