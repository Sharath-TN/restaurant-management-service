package com.restaurant.controller;

import com.restaurant.dto.StatusDTO;
import com.restaurant.dto.TableBookingDTO;
import com.restaurant.entity.TableBooking;
import com.restaurant.service.TableBookingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/table-booking")
@Slf4j
public class TableBookingController {

    private final TableBookingService tableBookingService;

    public TableBookingController(TableBookingService tableBookingService) {
        this.tableBookingService = tableBookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookTable(@RequestBody TableBookingDTO tableBookingDTO) {
        log.debug("Received request to book table: {}", tableBookingDTO);
        Long bookingId = tableBookingService.bookTable(tableBookingDTO);
        return ResponseEntity.ok("Table booked successfully with ID: " + bookingId);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TableBooking>> getAllBookings() {
        log.debug("Received request to get all table bookings");
        return ResponseEntity.ok(tableBookingService.getAllBookings());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TableBooking> getBookingById(@PathVariable Long id) {
        log.debug("Received request to get table booking by ID: {}", id);
        return ResponseEntity.ok(tableBookingService.getBookingById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable Long id, @Valid @RequestBody TableBookingDTO tableBookingDTO) {
        log.debug("Received request to update table booking with ID: {}, data: {}", id, tableBookingDTO);
        tableBookingService.updateBooking(id, tableBookingDTO);
        return ResponseEntity.ok("Booking updated successfully with ID: " + id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        log.debug("Received request to delete table booking with ID: {}", id);
        tableBookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking with ID: " + id + " deleted successfully");
    }

    @PutMapping("/updateStatus/{id}")
        public ResponseEntity<String> updateBookingStatus(@PathVariable Long id, @Valid @RequestBody StatusDTO status) {
        log.debug("Received request to update booking status for ID: {}, status: {}", id, status);
        tableBookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok("Booking status updated successfully with ID: " + id);
    }
}
