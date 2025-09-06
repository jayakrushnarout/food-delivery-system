package com.springcloud.delivery_service.service;

import com.springcloud.delivery_service.dto.DeliveryStatus;
import com.springcloud.delivery_service.entity.Delivery;
import com.springcloud.delivery_service.events.DeliveryRequest;
import com.springcloud.delivery_service.events.DeliveryResponse;
import com.springcloud.delivery_service.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService
{

    private final DeliveryRepository deliveryRepository;
    private final StreamBridge streamBridge;

    public void assignDelivery(DeliveryRequest request) {
        // Assign a random partner for now
        Delivery delivery = Delivery.builder()
                .orderId(request.getOrderId())
                .deliveryPartnerId(1001L) // Mock partner ID
                .status(DeliveryStatus.ASSIGNED)
                .build();

        delivery = deliveryRepository.save(delivery);

        // Mock delivery process
        delivery.setStatus(DeliveryStatus.DELIVERED);
        deliveryRepository.save(delivery);

        // Send response back to Order Service
        DeliveryResponse response = DeliveryResponse.builder()
                .orderId(delivery.getOrderId())
                .delivered(true)
                .message("Order delivered successfully")
                .build();

        streamBridge.send("deliveryResponse-out-0", response);
    }
}
