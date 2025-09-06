package com.springcloud.payment_service.entity;

import com.springcloud.payment_service.events.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long customerId; // optional if you want to store it
    private String provider; // MOCK, RAZORPAY, STRIPE

    private String currency = "INR"; // default currency
    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;
     private String FailureReason; // optional, for failed payments
    private String transactionId;


}
