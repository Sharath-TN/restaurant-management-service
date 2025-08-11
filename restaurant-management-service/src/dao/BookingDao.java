package dao;

import model.Booking;

import java.util.Map;

public interface BookingDao {
    int insertBooking(Booking booking);

    Map<Integer, String> getAllBookings();

    void markBookingAsComplete(int orderId);
}
