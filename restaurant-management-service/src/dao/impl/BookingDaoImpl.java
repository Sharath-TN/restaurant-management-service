package dao.impl;

import dao.BookingDao;
import model.Booking;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class BookingDaoImpl implements BookingDao {
    private final Connection connection;

    public BookingDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int insertBooking(Booking booking) {
        String sql = "INSERT INTO table_booking (customer_id, booking_time, num_people, is_active) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, booking.getCustomerId());
            stmt.setTimestamp(2, Timestamp.valueOf(booking.getBookingTime()));
            stmt.setInt(3, booking.getNumPeople());
            stmt.setBoolean(4, booking.getActive());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting booking: " + e.getMessage());
        }
        return -1;
    }

    public Map<Integer, String> getAllBookings() {
        Map<Integer, String> map = new LinkedHashMap<>();
        String sql = """
                SELECT b.id, c.name, b.booking_time 
                FROM table_booking b 
                JOIN customer c ON b.customer_id = c.id 
                WHERE b.is_active = true 
                ORDER BY b.booking_time
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String info = rs.getString("name") + " - " + rs.getTimestamp("booking_time");
                map.put(id, info);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching bookings: " + e.getMessage());
        }

        return map;
    }

    @Override
    public void markBookingAsComplete(int orderId) {
        String sql = "UPDATE table_booking SET is_active = FALSE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Booking marked as complete.");
            } else {
                System.out.println("No booking found with ID: " + orderId);
            }
        } catch (SQLException e) {
            System.out.println("Error marking booking as complete: " + e.getMessage());
        }
    }

}
