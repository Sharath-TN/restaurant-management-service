package com.restaurant.dto.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TableBookingDTO {
    private String customerName;
    private Long phoneNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingDate;
    private Long numberOfGuests;
}
