package com.springcloud.restaurant_service.controller;

import com.springcloud.restaurant_service.entity.Restaurant;
import com.springcloud.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // Create Restaurant
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.createRestaurant(restaurant));
    }

    // Get All Restaurants
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping(value="/getAll")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    // Get Restaurant by Id
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/id/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    // Update Restaurant
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/upadte/id/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
                                                       @RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurant));
    }

    // Delete Restaurant
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
