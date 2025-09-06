package com.springcloud.delivery_service.events;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryRequest
{
    private Long orderId;              // Order identifier
    private Long customerId;           // Customer identifier
    private String deliveryAddress;    // Delivery location
}
