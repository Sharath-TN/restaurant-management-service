package com.restaurant.service;

import com.restaurant.dto.StatusDTO;
import com.restaurant.dto.TableBookingDTO;
import com.restaurant.entity.TableBooking;

import java.util.List;

public interface TableBookingService {
    List<TableBooking> getAllBookings();

    TableBooking getBookingById(Long id);

    void updateBooking(Long id, TableBookingDTO request);

    void deleteBooking(Long id);

    void updateBookingStatus(Long id, StatusDTO status);
}
