package com.restaurant.service.impl;

import com.restaurant.dto.MenuItemDTO;
import com.restaurant.entity.MenuItem;
import com.restaurant.repository.MenuItemRepository;
import com.restaurant.service.MenuItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void addMenuItem(MenuItemDTO request) {
        log.debug("Received request to add menu item: {}", request);
        MenuItem menuItem = MenuItem.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();
        log.info("Adding menu item with name: {}", request.getName());
        menuItemRepository.save(menuItem);
    }

    @Override
    public void deleteMenuItem(Long id) {
        log.debug("Received request to delete menu item with ID: {}", id);
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Menu item with ID {} not found", id);
                    return new RuntimeException("Menu item not found");
                });
        log.info("Deleting menu item with name: {}", menuItem.getName());
        menuItemRepository.delete(menuItem);
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        log.debug("Received request to get all menu items");
        return menuItemRepository.findAll();
    }

    @Override
    public void updateMenuItem(MenuItemDTO request) {
        log.debug("Received request to update menu item: {}", request);
        menuItemRepository.findByName(request.getName())
                .ifPresentOrElse(menuItem -> {
                    if(request.getPrice() != null) {
                        menuItem.setPrice(request.getPrice());
                    }
                    log.info("Updating menu item with name: {}", request.getName());
                    menuItemRepository.save(menuItem);
                }, () -> {
                    log.error("Menu item with name {} not found", request.getName());
                    throw new RuntimeException("Menu item not found");
                });
    }
}
