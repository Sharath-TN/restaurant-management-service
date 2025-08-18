package com.restaurant.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class StatusDTO {
    @NotEmpty
    private String status;
}
