package dao.impl;

import dao.OrderDao;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private final Connection connection;

    public OrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int insertOrder(Order order) {
        String sql = "INSERT INTO orders (table_booking_id, waiter_name, status) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, order.getBookingId());
            stmt.setString(2, order.getWaiterName());
            stmt.setString(3, order.getStatus());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public List<Order> getPendingOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = 'Pending' ORDER BY created_at";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setBookingId(rs.getInt("table_booking_id"));
                order.setWaiterName(rs.getString("waiter_name"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                orders.add(order);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching pending orders: " + e.getMessage());
        }

        return orders;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating order status: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Order> getPreparedOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = 'Prepared' ORDER BY created_at";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setBookingId(rs.getInt("table_booking_id"));
                order.setWaiterName(rs.getString("waiter_name"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                orders.add(order);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching prepared orders: " + e.getMessage());
        }

        return orders;
    }
}
