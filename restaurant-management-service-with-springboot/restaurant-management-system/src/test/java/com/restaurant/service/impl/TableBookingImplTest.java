package com.restaurant.service.impl;

import com.restaurant.dto.StatusDTO;
import com.restaurant.dto.TableBookingDTO;
import com.restaurant.entity.TableBooking;
import com.restaurant.enumeration.BookingStatus;
import com.restaurant.repository.TableBookingRepository;
import com.restaurant.service.TableBookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static com.restaurant.util.TestUtil.TEST_TABLE_BOOKING_ID;
import static com.restaurant.util.TestUtil.getTableBookingDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class TableBookingImplTest {
    @Mock
    private TableBookingRepository tableBookingRepository;

    private TableBookingService tableBookingService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        tableBookingService = new TableBookingServiceImpl(tableBookingRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testBookTable_ShouldSaveBookingAndReturnId() {
        TableBookingDTO request = new TableBookingDTO();
        request.setCustomerName("John Doe");
        request.setPhoneNumber(1234567890L);
        request.setBookingDate(LocalDate.now().atStartOfDay());
        request.setNumberOfGuests(4L);

        TableBooking savedBooking = TableBooking.builder()
                .customerName("John Doe")
                .phoneNumber(1234567890L)
                .bookingDate(request.getBookingDate())
                .numberOfGuests(4L)
                .bookingStatus(BookingStatus.PENDING)
                .id(1L)
                .build();

        when(tableBookingRepository.save(any(TableBooking.class))).thenReturn(savedBooking);

        Long bookingId = tableBookingService.bookTable(request);

        assertEquals(1L, bookingId);
        Mockito.verify(tableBookingRepository, times(1)).save(any(TableBooking.class));
    }

    @Test
    public void testGetAllBookings_ShouldReturnListOfBookings() {
        TableBooking booking1 = TableBooking.builder().id(1L).build();
        TableBooking booking2 = TableBooking.builder().id(2L).build();
        when(tableBookingRepository.findAll()).thenReturn(List.of(booking1, booking2));

        List<TableBooking> bookings = tableBookingService.getAllBookings();

        assertEquals(2, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(2L, bookings.get(1).getId());
    }

    @Test
    public void testGetBookingById_ShouldReturnBooking_WhenExists() {
        TableBooking booking = TableBooking.builder().id(1L).build();
        when(tableBookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));

        TableBooking result = tableBookingService.getBookingById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetBookingById_ShouldThrowException_WhenNotFound() {
        when(tableBookingRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> tableBookingService.getBookingById(99L)
        );
        assertEquals("Booking not found with id: 99", exception.getMessage());
    }

    @Test
    public void testUpdateBooking_ShouldUpdateFields_WhenFieldsAreNotNull() {
        TableBooking existingBooking = TableBooking.builder()
                .id(1L)
                .customerName("Old Name")
                .phoneNumber(1111111111L)
                .bookingDate(LocalDate.now().atStartOfDay())
                .numberOfGuests(2L)
                .bookingStatus(BookingStatus.PENDING)
                .build();

        TableBookingDTO request = new TableBookingDTO();
        request.setCustomerName("New Name");
        request.setPhoneNumber(2222222222L);
        request.setBookingDate(LocalDate.now().plusDays(1).atStartOfDay());
        request.setNumberOfGuests(4L);

        when(tableBookingRepository.findById(1L)).thenReturn(java.util.Optional.of(existingBooking));
        when(tableBookingRepository.save(any(TableBooking.class))).thenReturn(existingBooking);

        tableBookingService.updateBooking(1L, request);

        assertEquals("New Name", existingBooking.getCustomerName());
        assertEquals(2222222222L, existingBooking.getPhoneNumber());
        assertEquals(LocalDate.now().plusDays(1).atStartOfDay(), existingBooking.getBookingDate());
        assertEquals(4L, existingBooking.getNumberOfGuests());
        Mockito.verify(tableBookingRepository, times(1)).save(existingBooking);
    }

    @Test
    public void testDeleteBooking_ShouldDeleteBooking_WhenExists() {
        TableBooking booking = TableBooking.builder().id(1L).build();
        when(tableBookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));

        tableBookingService.deleteBooking(1L);

        Mockito.verify(tableBookingRepository, times(1)).delete(booking);
    }

    @Test
    public void testUpdateBookingStatus_ShouldUpdateStatus() {
        TableBooking booking = TableBooking.builder()
                .id(1L)
                .bookingStatus(BookingStatus.PENDING)
                .build();
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatus("CONFIRMED");

        when(tableBookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));
        when(tableBookingRepository.save(any(TableBooking.class))).thenReturn(booking);

        tableBookingService.updateBookingStatus(1L, statusDTO);

        assertEquals(BookingStatus.CONFIRMED, booking.getBookingStatus());
        Mockito.verify(tableBookingRepository, times(1)).save(booking);
    }
}
