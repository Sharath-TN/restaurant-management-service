package com.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeta_training.restaurant_management_system.enumeration.BookingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Booking")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    @SequenceGenerator(name = "booking_seq", sequenceName = "booking_sequence", initialValue = 100, allocationSize = 1)
    private Long id;

    @NotNull(message = "Customer name is required")
    private String customerName;

    @NotNull(message = "phone number is required")
    @Digits(integer = 10, fraction = 0)
    private Long phoneNumber;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingDate;

    @NotNull
    private Long NumberOfGuests;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
}
