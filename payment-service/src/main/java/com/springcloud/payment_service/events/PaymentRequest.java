package com.springcloud.payment_service.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Long orderId;
    private Long customerId;      // optional
    private double amount;
    private String provider;      // e.g., "MOCK" (default), "RAZORPAY", "STRIPE"
}
