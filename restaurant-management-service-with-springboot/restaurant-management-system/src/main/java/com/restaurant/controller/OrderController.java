package com.restaurant.controller;

import com.restaurant.dto.OrderRequest;
import com.restaurant.dto.StatusDTO;
import com.restaurant.entity.Order;
import com.restaurant.enumeration.OrderItemStatus;
import com.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest request) {
        Order savedOrder = orderService.placeOrUpdateOrder(request);
        return ResponseEntity.ok(savedOrder);
    }

    @PutMapping("/updateOrderItem/{orderItemId}")
    public ResponseEntity<String> updateOrderItem(@PathVariable Long orderItemId, @RequestBody StatusDTO status) {
        orderService.updateOrderItemStatus(orderItemId, OrderItemStatus.valueOf(status.getStatus().toUpperCase()));
        return ResponseEntity.ok("Order item status updated successfully");
    }
}

