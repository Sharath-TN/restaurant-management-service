package dao;

import model.Order;

import java.util.List;

public interface OrderDao {
    int insertOrder(Order order);

    List<Order> getPendingOrders();

    boolean updateOrderStatus(int orderId, String status);

    List<Order> getPreparedOrders();
}
