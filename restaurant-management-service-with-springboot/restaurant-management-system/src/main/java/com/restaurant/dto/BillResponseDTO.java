package com.restaurant.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillResponseDTO {
    private Long orderId;
    private Double totalAmount;
    private LocalDateTime generatedAt;
    private List<OrderItemResponseDTO> orderItems;
}
