package com.springcloud.delivery_service.entity;

import com.springcloud.delivery_service.dto.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long deliveryPartnerId;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
