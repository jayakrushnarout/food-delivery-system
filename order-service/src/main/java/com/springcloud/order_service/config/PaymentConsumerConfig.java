package com.springcloud.order_service.config;

import com.springcloud.order_service.events.PaymentRequest;
import com.springcloud.order_service.events.PaymentResponse;
import com.springcloud.order_service.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class PaymentConsumerConfig
{

    @Bean
    public Consumer<PaymentResponse> paymentResponse(OrderService service)
    {
        return service::handlePaymentResponse;
    }
}
