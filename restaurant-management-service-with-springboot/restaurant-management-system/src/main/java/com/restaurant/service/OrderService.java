package com.restaurant.service;

import com.restaurant.dto.BillResponseDTO;
import com.restaurant.dto.OrderRequest;
import com.restaurant.entity.Order;
import com.restaurant.enumeration.OrderItemStatus;
import jakarta.transaction.Transactional;

public interface OrderService {
    @Transactional
    Order placeOrUpdateOrder(OrderRequest request);

    void updateOrderItemStatus(Long orderItemId, OrderItemStatus status);

    @Transactional
    BillResponseDTO generateBill(Long orderId);
}
