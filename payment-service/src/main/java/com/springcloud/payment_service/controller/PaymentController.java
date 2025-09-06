package com.springcloud.payment_service.controller;

import com.springcloud.payment_service.entity.Payment;
import com.springcloud.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{orderId}")
    public Payment getByOrder(@PathVariable Long orderId)
    {
        return paymentService.getByOrderId(orderId);
    }
}
