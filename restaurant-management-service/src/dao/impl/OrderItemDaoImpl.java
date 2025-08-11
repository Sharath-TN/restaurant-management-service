package dao.impl;

import dao.OrderItemDao;
import model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoImpl implements OrderItemDao {
    private final Connection connection;

    public OrderItemDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int insertOrderItem(OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, menu_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, item.getOrderId());
            stmt.setInt(2, item.getMenuId());
            stmt.setInt(3, item.getQuantity());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting order item: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("menu_id"),
                        rs.getInt("quantity")
                );
                list.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching order items: " + e.getMessage());
        }

        return list;
    }
}
