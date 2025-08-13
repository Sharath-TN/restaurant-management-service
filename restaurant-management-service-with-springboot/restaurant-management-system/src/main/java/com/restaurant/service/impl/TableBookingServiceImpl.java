package com.restaurant.service.impl;

import com.restaurant.dto.StatusDTO;
import com.restaurant.dto.TableBookingDTO;
import com.restaurant.entity.TableBooking;
import com.restaurant.enumeration.BookingStatus;
import com.restaurant.repository.TableBookingRepository;
import com.restaurant.service.TableBookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableBookingServiceImpl implements TableBookingService {
    private final TableBookingRepository tableBookingRepository;

    public TableBookingServiceImpl(TableBookingRepository tableBookingRepository) {
        this.tableBookingRepository = tableBookingRepository;
    }

    @Override
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

    @Override
    public List<TableBooking> getAllBookings() {
        return tableBookingRepository.findAll();
    }

    @Override
    public TableBooking getBookingById(Long id) {
        return tableBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
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

    @Override
    public void deleteBooking(Long id) {
        TableBooking existingBooking = getBookingById(id);
        tableBookingRepository.delete(existingBooking);
    }

    @Override
    public void updateBookingStatus(Long id, StatusDTO status) {
        TableBooking existingBooking = getBookingById(id);
        BookingStatus bookingStatus = BookingStatus.valueOf(status.getStatus().toUpperCase());
        existingBooking.setBookingStatus(bookingStatus);
        tableBookingRepository.save(existingBooking);
    }
}
