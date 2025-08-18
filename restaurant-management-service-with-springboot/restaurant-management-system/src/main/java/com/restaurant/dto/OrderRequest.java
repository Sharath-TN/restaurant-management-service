package com.restaurant.dto;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class OrderRequest {
    private Long tableBookingId;
    private List<OrderItemRequest> items;
}