package com.springcloud.restaurant_service.config;

import com.springcloud.restaurant_service.events.RestaurantValidationRequest;
import com.springcloud.restaurant_service.service.RestaurantService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

@Configuration
public class RestaurantConsumerConfig
{

    @Bean
    public Consumer<RestaurantValidationRequest> restaurantValidation(RestaurantService service) {
        return service::handleValidationRequest;
    }
}
