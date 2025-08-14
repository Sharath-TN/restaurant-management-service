package com.restaurant.controller;

import com.restaurant.dto.MenuItemDTO;
import com.restaurant.entity.MenuItem;
import com.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menuItem")
public class MenuItemController {
    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping("/addMenuItem")
    public ResponseEntity<String> addMenuItem(@RequestBody MenuItemDTO request) {
        menuItemService.addMenuItem(request);
        return ResponseEntity.ok("Menu item added successfully");
    }

    @GetMapping("/getAllMenuItems")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @DeleteMapping("/deleteMenuItem/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok("Menu item deleted successfully");
    }

    @PatchMapping("/updateMenuItem")
    public ResponseEntity<String> updateMenuItem(@RequestBody MenuItemDTO request) {
        menuItemService.updateMenuItem(request);
        return ResponseEntity.ok("Menu item updated successfully");
    }
}
