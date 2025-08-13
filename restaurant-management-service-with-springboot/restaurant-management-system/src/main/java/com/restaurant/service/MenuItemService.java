package com.restaurant.service;

import com.restaurant.dto.MenuItemDTO;
import com.restaurant.entity.MenuItem;

import java.util.List;

public interface MenuItemService {
    void addMenuItem(MenuItemDTO request);

    void deleteMenuItem(Long id);

    List<MenuItem> getAllMenuItems();
}
