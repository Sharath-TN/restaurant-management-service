package com.restaurant.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuItemDTO {
    @NotEmpty
    private String name;
    @NotNull
    private Double price;
}
