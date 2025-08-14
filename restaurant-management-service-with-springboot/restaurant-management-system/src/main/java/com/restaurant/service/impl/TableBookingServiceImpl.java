package com.restaurant.service.impl;

import com.restaurant.dto.StatusDTO;
import com.restaurant.dto.TableBookingDTO;
import com.restaurant.entity.TableBooking;
import com.restaurant.enumeration.BookingStatus;
import com.restaurant.repository.TableBookingRepository;
import com.restaurant.service.TableBookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TableBookingServiceImpl implements TableBookingService {
    private final TableBookingRepository tableBookingRepository;

    public TableBookingServiceImpl(TableBookingRepository tableBookingRepository) {
        this.tableBookingRepository = tableBookingRepository;
    }

    @Override
    public Long bookTable(TableBookingDTO request) {
        log.debug("Received request to book table: {}", request);
        TableBooking tableBooking = TableBooking.builder()
                .customerName(request.getCustomerName())
                .phoneNumber(request.getPhoneNumber())
                .bookingDate(request.getBookingDate())
                .numberOfGuests(request.getNumberOfGuests())
                .bookingStatus(BookingStatus.PENDING)
                .build();
        tableBooking = tableBookingRepository.save(tableBooking);
        log.info("Table booked successfully with ID: {}", tableBooking.getId());
        return tableBooking.getId();
    }

    @Override
    public List<TableBooking> getAllBookings() {
        log.debug("Received request to get all table bookings");
        return tableBookingRepository.findAll();
    }

    @Override
    public TableBooking getBookingById(Long id) {
        log.debug("Received request to get table booking by ID: {}", id);
        return tableBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    public void updateBooking(Long id, TableBookingDTO request) {
        log.debug("Received request to update table booking with ID: {}, data: {}", id, request);
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

    @Override
    public void deleteBooking(Long id) {
        log.debug("Received request to delete table booking with ID: {}", id);
        TableBooking existingBooking = getBookingById(id);
        tableBookingRepository.delete(existingBooking);
    }

    @Override
    public void updateBookingStatus(Long id, StatusDTO status) {
        log.debug("Received request to update booking status for ID: {}, status: {}", id, status);
        TableBooking existingBooking = getBookingById(id);
        BookingStatus bookingStatus = BookingStatus.valueOf(status.getStatus().toUpperCase());
        existingBooking.setBookingStatus(bookingStatus);
        log.info("Updating booking status for ID: {} to {}", id, bookingStatus);
        tableBookingRepository.save(existingBooking);
    }
}
