package com.springcloud.restaurant_service.service;

import com.springcloud.restaurant_service.entity.MenuItem;
import com.springcloud.restaurant_service.entity.Restaurant;
import com.springcloud.restaurant_service.events.RestaurantValidationRequest;
import com.springcloud.restaurant_service.events.RestaurantValidationResponse;
import com.springcloud.restaurant_service.repository.MenuItemRepository;
import com.springcloud.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final StreamBridge streamBridge;

    // Handle validation requests
    public void handleValidationRequest(RestaurantValidationRequest request) {
        RestaurantValidationResponse response = new RestaurantValidationResponse();
        response.setRestaurantId(request.getRestaurantId());
        response.setOrderId(request.getOrderId());

        // Fetch restaurant by ID
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElse(null);

        if (restaurant == null) {
            response.setValid(false);
            response.setMessage("Restaurant does not exist");
        } else if (!Boolean.TRUE.equals(restaurant.getOpen())) {
            response.setValid(false);
            response.setMessage("Restaurant is currently closed");
        } else {
            // Check if all ordered menu items exist and are available
            boolean allItemsAvailable = restaurant.getMenuItems().stream()
                    .filter(item -> request.getMenuItemIds().contains(item.getId()))
                    .allMatch(MenuItem::getAvailable);

            if (!allItemsAvailable) {
                response.setValid(false);
                response.setMessage("Some menu items are unavailable");
            } else {
                response.setValid(true);
                response.setMessage("Restaurant and menu items are valid and available");
            }
        }

        // Send response back to order service
        streamBridge.send("restaurantValidation-out-0", response);
    }


    public List<Restaurant> getAllRestaurants()
    {
        return restaurantRepository.findAll();
    }


    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        Restaurant saved = restaurantRepository.save(restaurant);
        return saved;
    }

    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        Restaurant existing = getRestaurantById(id);
        existing.setName(restaurant.getName());
        existing.setAddress(restaurant.getAddress());
        existing.setCategory(restaurant.getCategory());
        existing.setOpen(restaurant.getOpen());
        existing.setMenuItems(restaurant.getMenuItems());
        Restaurant updated = restaurantRepository.save(existing);
        return updated;
    }

    public void deleteRestaurant(Long id) {
        Restaurant existing = getRestaurantById(id);
        restaurantRepository.delete(existing);
    }
}
