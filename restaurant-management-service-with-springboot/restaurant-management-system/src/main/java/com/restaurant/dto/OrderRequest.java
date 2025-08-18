package com.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class OrderRequest {
    @NotNull
    private Long tableBookingId;
    private List<OrderItemRequest> items;
}