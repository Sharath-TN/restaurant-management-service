package com.restaurant.service;

import com.restaurant.dto.MenuItemDTO;

public interface MenuItemService {
    void addMenuItem(MenuItemDTO request);

    void deleteMenuItem(Long id);
}
