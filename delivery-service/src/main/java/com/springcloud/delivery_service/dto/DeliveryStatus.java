package com.springcloud.delivery_service.dto;

public enum DeliveryStatus {
    ASSIGNED,       // Delivery partner assigned
    PICKED_UP,      // Order picked up from restaurant
    IN_TRANSIT,     // On the way
    DELIVERED,      // Successfully delivered
    FAILED          // Could not be delivered
}
