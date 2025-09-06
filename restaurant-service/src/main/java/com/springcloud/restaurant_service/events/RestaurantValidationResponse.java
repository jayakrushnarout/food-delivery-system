package com.springcloud.restaurant_service.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantValidationResponse {
    private Long restaurantId;
    private Long orderId;
    private boolean valid;
    private String message;
}
