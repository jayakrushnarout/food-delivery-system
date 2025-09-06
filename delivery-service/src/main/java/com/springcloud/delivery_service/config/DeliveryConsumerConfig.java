package com.springcloud.delivery_service.config;

import com.springcloud.delivery_service.events.DeliveryRequest;
import com.springcloud.delivery_service.service.DeliveryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class DeliveryConsumerConfig {

    @Bean
    public Consumer<DeliveryRequest> deliveryRequest(DeliveryService service)
    {
        return service::assignDelivery;
    }
}
