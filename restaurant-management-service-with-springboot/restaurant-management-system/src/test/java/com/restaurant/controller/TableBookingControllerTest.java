package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.dto.StatusDTO;
import com.restaurant.dto.TableBookingDTO;
import com.restaurant.entity.TableBooking;
import com.restaurant.enumeration.BookingStatus;
import com.restaurant.service.TableBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class TableBookingControllerTest {

    @Mock
    private TableBookingService tableBookingService;

    private TableBookingController tableBookingController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        tableBookingController = new TableBookingController(tableBookingService);
        mockMvc = MockMvcBuilders.standaloneSetup(tableBookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testBookTable() throws Exception {
        // Setup
        Long bookingId = 100L;
        Mockito.when(tableBookingService.bookTable(any(TableBookingDTO.class))).thenReturn(bookingId);

        // Create request
        MockHttpServletRequestBuilder mockRequest = post("/table-booking/book")
                .contentType("application/json")
                .content("{\"customerName\":\"John Doe\",\"phoneNumber\":9876543210,\"bookingDate\":\"2023-07-10 18:30\",\"numberOfGuests\":4}");

        // Perform request and verify
        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        String response = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("Table booked successfully with ID: 100", response);

        // Verify service method was called
        Mockito.verify(tableBookingService).bookTable(any(TableBookingDTO.class));
    }

    @Test
    public void testGetAllBookings() throws Exception {
        // Setup mock data
        TableBooking booking1 = new TableBooking();
        booking1.setId(100L);
        booking1.setCustomerName("John Doe");
        booking1.setPhoneNumber(9876543210L);
        booking1.setBookingDate(LocalDateTime.now());
        booking1.setNumberOfGuests(4L);
        booking1.setBookingStatus(BookingStatus.CONFIRMED);

        TableBooking booking2 = new TableBooking();
        booking2.setId(101L);
        booking2.setCustomerName("Jane Smith");
        booking2.setPhoneNumber(8765432109L);
        booking2.setBookingDate(LocalDateTime.now().plusDays(1));
        booking2.setNumberOfGuests(2L);
        booking2.setBookingStatus(BookingStatus.PENDING);

        List<TableBooking> bookings = Arrays.asList(booking1, booking2);

        // Mock service response
        Mockito.when(tableBookingService.getAllBookings()).thenReturn(bookings);

        // Create request
        MockHttpServletRequestBuilder mockRequest = get("/table-booking/getAll")
                .contentType("application/json");

        // Perform request
        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        // Verify response
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        List<TableBooking> responseBookings = objectMapper.readValue(responseContent,
                objectMapper.getTypeFactory().constructCollectionType(List.class, TableBooking.class));

        assertEquals(2, responseBookings.size());
        assertEquals("John Doe", responseBookings.get(0).getCustomerName());
        assertEquals("Jane Smith", responseBookings.get(1).getCustomerName());

        // Verify service method was called
        Mockito.verify(tableBookingService).getAllBookings();
    }

    @Test
    public void testGetBookingById() throws Exception {
        // Setup mock data
        Long bookingId = 100L;
        TableBooking booking = new TableBooking();
        booking.setId(bookingId);
        booking.setCustomerName("John Doe");
        booking.setPhoneNumber(9876543210L);
        booking.setBookingDate(LocalDateTime.now());
        booking.setNumberOfGuests(4L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        // Mock service response
        Mockito.when(tableBookingService.getBookingById(bookingId)).thenReturn(booking);

        // Create request
        MockHttpServletRequestBuilder mockRequest = get("/table-booking/get/{id}", bookingId)
                .contentType("application/json");

        // Perform request
        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        // Verify response
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        TableBooking responseBooking = objectMapper.readValue(responseContent, TableBooking.class);

        assertEquals(bookingId, responseBooking.getId());
        assertEquals("John Doe", responseBooking.getCustomerName());
        assertEquals(9876543210L, responseBooking.getPhoneNumber());
        assertEquals(4L, responseBooking.getNumberOfGuests());

        // Verify service method was called
        Mockito.verify(tableBookingService).getBookingById(bookingId);
    }

    @Test
    public void testUpdateBooking() throws Exception {
        // Setup
        Long bookingId = 100L;

        // Create request
        MockHttpServletRequestBuilder mockRequest = put("/table-booking/update/{id}", bookingId)
                .contentType("application/json")
                .content("{\"customerName\":\"Updated Name\",\"phoneNumber\":8765432109,\"bookingDate\":\"2023-07-15 19:00\",\"numberOfGuests\":6}");

        // Perform request
        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        // Verify response
        String response = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("Booking updated successfully with ID: 100", response);

        // Verify service method was called
        Mockito.verify(tableBookingService).updateBooking(eq(bookingId), any(TableBookingDTO.class));
    }

    @Test
    public void testDeleteBooking() throws Exception {
        // Setup
        Long bookingId = 100L;

        // Create request
        MockHttpServletRequestBuilder mockRequest = delete("/table-booking/delete/{id}", bookingId)
                .contentType("application/json");

        // Perform request
        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        // Verify response
        String response = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("Booking with ID: 100 deleted successfully", response);

        // Verify service method was called
        Mockito.verify(tableBookingService).deleteBooking(bookingId);
    }

    @Test
    public void testUpdateBookingStatus() throws Exception {
        // Setup
        Long bookingId = 100L;

        // Create request
        MockHttpServletRequestBuilder mockRequest = put("/table-booking/updateStatus/{id}", bookingId)
                .contentType("application/json")
                .content("{\"status\":\"CONFIRMED\"}");

        // Perform request
        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        // Verify response
        String response = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("Booking status updated successfully with ID: 100", response);

        // Verify service method was called
        Mockito.verify(tableBookingService).updateBookingStatus(eq(bookingId), any(StatusDTO.class));
    }
}