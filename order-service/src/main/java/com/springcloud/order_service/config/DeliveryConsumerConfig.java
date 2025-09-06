package com.springcloud.order_service.config;

import com.springcloud.order_service.events.DeliveryResponse;
import com.springcloud.order_service.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class DeliveryConsumerConfig
{

    @Bean
    public Consumer<DeliveryResponse> deliveryResponse(OrderService service) {
        return service::handleDeliveryResponse;
    }



}