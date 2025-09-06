package com.springcloud.payment_service.service;

import com.springcloud.payment_service.events.PaymentStatus;
import com.springcloud.payment_service.entity.Payment;
import com.springcloud.payment_service.events.PaymentRequest;
import com.springcloud.payment_service.events.PaymentResponse;
import com.springcloud.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StreamBridge streamBridge;
    private final Random random = new Random();

    // Consumer entry point (called by functional bean)
    public void handlePaymentRequest(PaymentRequest req) {
        // persist initial record
        Payment payment = Payment.builder()
                .orderId(req.getOrderId())
                .customerId(req.getCustomerId())
                .provider(req.getProvider() == null ? "MOCK" : req.getProvider())
                .amount(req.getAmount())
                .status(PaymentStatus.PENDING)
                .build();
        payment = paymentRepository.save(payment);

        // mock gateway processing
        boolean ok = processWithMockGateway(payment);

        // update + emit event
        if (ok) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setFailureReason(null);
            paymentRepository.save(payment);

            PaymentResponse response = PaymentResponse.builder()
                    .orderId(payment.getOrderId())
                    .status(PaymentStatus.SUCCESS)
                    .transactionId(payment.getTransactionId())
                    .message("Payment captured")
                    .build();
            streamBridge.send("paymentResponse-out-0", response);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setTransactionId(null);
            payment.setFailureReason("Insufficient funds (mock)");
            paymentRepository.save(payment);

            PaymentResponse response = PaymentResponse.builder()
                    .orderId(payment.getOrderId())
                    .status(PaymentStatus.FAILED)
                    .transactionId(null)
                    .message(payment.getFailureReason())
                    .build();
            streamBridge.send("paymentResponse-out-0", response);
        }
    }

    // Simulate external payment gateway call
    private boolean processWithMockGateway(Payment payment) {
        // 85% success rate mock
        return random.nextInt(100) < 85;
    }

    public Payment getByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId).orElse(null);
    }
}
