package com.restaurant.service;

import com.restaurant.dto.StatusDTO;
import com.restaurant.dto.TableBookingDTO;
import com.restaurant.entity.TableBooking;
import com.restaurant.enumeration.BookingStatus;
import com.restaurant.repository.TableBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableBookingService {
    @Autowired
    private TableBookingRepository tableBookingRepository;

    public Long bookTable(TableBookingDTO request) {
        TableBooking tableBooking = TableBooking.builder()
                .customerName(request.getCustomerName())
                .phoneNumber(request.getPhoneNumber())
                .bookingDate(request.getBookingDate())
                .NumberOfGuests(request.getNumberOfGuests())
                .bookingStatus(BookingStatus.PENDING)
                .build();
        return tableBookingRepository.save(tableBooking).getId();
    }

    public List<TableBooking> getAllBookings() {
        return tableBookingRepository.findAll();
    }

    public TableBooking getBookingById(Long id) {
        return tableBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    public void updateBooking(Long id, TableBookingDTO request) {
        TableBooking existingBooking = getBookingById(id);
        if(request.getCustomerName()!=null) {
            existingBooking.setCustomerName(request.getCustomerName());
        }
        if(request.getPhoneNumber()!=null) {
            existingBooking.setPhoneNumber(request.getPhoneNumber());
        }
        if(request.getBookingDate()!=null) {
            existingBooking.setBookingDate(request.getBookingDate());
        }
        if(request.getNumberOfGuests()!=null){
        existingBooking.setNumberOfGuests(request.getNumberOfGuests());
        }
        tableBookingRepository.save(existingBooking);
    }

    public void deleteBooking(Long id) {
        TableBooking existingBooking = getBookingById(id);
        tableBookingRepository.delete(existingBooking);
    }

    public void updateBookingStatus(Long id, StatusDTO status) {
        TableBooking existingBooking = getBookingById(id);
        BookingStatus bookingStatus = BookingStatus.valueOf(status.getStatus().toUpperCase());
        existingBooking.setBookingStatus(bookingStatus);
        tableBookingRepository.save(existingBooking);
    }
}
