package com.springcloud.restaurant_service.controller;

import com.springcloud.restaurant_service.entity.MenuItem;
import com.springcloud.restaurant_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    // Create Menu Item under a restaurant
    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<MenuItem> createMenuItem(@PathVariable Long restaurantId,
                                                   @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(menuItemService.addMenuItem(restaurantId, menuItem));
    }

    @GetMapping("/restaurantId/{restaurantId}")
    public List<MenuItem> getMenuItems(@PathVariable Long restaurantId) {
        return menuItemService.getMenuItemsByRestaurant(restaurantId);
    }

    // Get all Menu Items
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    // Get all menu items for a restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByRestaurant(restaurantId));
    }

    // Get menu item by id
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    // Update Menu Item
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
                                                   @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, menuItem));
    }

    // Delete Menu Item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}
