package com.springcloud.restaurant_service.service;

import com.springcloud.restaurant_service.entity.MenuItem;
import com.springcloud.restaurant_service.repository.MenuItemRepository;
import com.springcloud.restaurant_service.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@AllArgsConstructor
@Service
@Transactional
public class MenuItemService {


    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepo;


    public List<MenuItem> getAllMenuItems()
    {
        return menuItemRepository.findAll();
    }

    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
    }

    public MenuItem addMenuItem(Long restaurantId, MenuItem menuItem)
    {
        var restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        menuItem.setRestaurant(restaurant);
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(Long id, MenuItem menuItem) {
        MenuItem existing = getMenuItemById(id);
        existing.setName(menuItem.getName());
        existing.setDescription(menuItem.getDescription());
        existing.setPrice(menuItem.getPrice());
        existing.setAvailable(menuItem.getAvailable());
        return menuItemRepository.save(existing);
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId)
    {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
}
