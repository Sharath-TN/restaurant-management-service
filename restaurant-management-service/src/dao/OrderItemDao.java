package dao;

import model.OrderItem;

import java.util.List;

public interface OrderItemDao {
    int insertOrderItem(OrderItem item);

    List<OrderItem> getOrderItemsByOrderId(int orderId);
}
