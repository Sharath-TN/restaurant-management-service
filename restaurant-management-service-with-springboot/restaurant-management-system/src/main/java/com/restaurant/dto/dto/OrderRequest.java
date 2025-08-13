package com.restaurant.dto.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Long tableBookingId;
    private List<OrderItemRequest> items;
}

