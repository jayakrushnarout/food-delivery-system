package com.springcloud.order_service.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long orderId;
    private PaymentStatus status;         // SUCCESS | FAILED
    private String transactionId;  // non-null if success
    private String message;        // error or info
}
