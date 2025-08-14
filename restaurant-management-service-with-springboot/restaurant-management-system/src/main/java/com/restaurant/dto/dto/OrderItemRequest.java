package com.restaurant.dto.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long menuItemId;
    private Long quantity;
}
