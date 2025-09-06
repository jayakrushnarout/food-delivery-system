package com.springcloud.order_service.config;

import com.springcloud.order_service.events.RestaurantValidationResponse;
import com.springcloud.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OrderConsumerConfig {

    @Bean
    public Consumer<RestaurantValidationResponse> restaurantValidationConsumer(OrderService orderService)
    {
        return (restaurantValidationResponse) -> {
            log.info("Received Restaurant Validation Response: {}", restaurantValidationResponse);
            orderService.handleRestaurantValidation(restaurantValidationResponse);
        };
    }
	
}
