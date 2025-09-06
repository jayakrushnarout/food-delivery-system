package com.springcloud.restaurant_service.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantValidationRequest
{
    private Long orderId; // Optional, used for order-specific validation
    private Long restaurantId;
    private List<Long> menuItemIds;
}
