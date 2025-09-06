package com.springcloud.order_service.events;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryResponse {
    private Long orderId;
    private boolean delivered;
    private String message;
}
