package com.springcloud.order_service.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantValidationRequest
{
    private Long restaurantId;
    private Long orderId;
    private List<Long> menuItemIds;
}
