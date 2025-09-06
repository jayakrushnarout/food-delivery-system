package com.springcloud.payment_service.config;

import com.springcloud.payment_service.events.PaymentRequest;
import com.springcloud.payment_service.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class PaymentConsumerConfig
{
    @Bean
    public Consumer<PaymentRequest> paymentRequest(PaymentService service) {
        return service::handlePaymentRequest;
    }

}
