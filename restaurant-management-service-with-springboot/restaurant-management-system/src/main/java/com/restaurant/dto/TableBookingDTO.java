package com.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TableBookingDTO {
    @NotEmpty
    private String customerName;
    @NotEmpty
    private Long phoneNumber;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingDate;
    @NotNull
    private Long numberOfGuests;
}
