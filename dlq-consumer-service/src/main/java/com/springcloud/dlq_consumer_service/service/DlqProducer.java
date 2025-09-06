package com.springcloud.dlq_consumer_service.service;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class DlqProducer {

    private final StreamBridge streamBridge;

    public DlqProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void reprocessMessage(String dlqTopic, String message) {
        String bindingName = mapToBinding(dlqTopic);
        streamBridge.send(bindingName, message);
    }

    private String mapToBinding(String dlqTopic) {
        switch (dlqTopic) {
            case "delivery.requests.dlq": return "deliveryRequests-out-0";
            case "delivery.responses.dlq": return "deliveryResponses-out-0";
            case "payment.requests.dlq": return "paymentRequests-out-0";
            case "payment.responses.dlq": return "paymentResponses-out-0";
            case "restaurant.validation.requests.dlq": return "restaurantValidationRequests-out-0";
            case "restaurant.validation.responses.dlq": return "restaurantValidationResponses-out-0";
            default: throw new IllegalArgumentException("Unknown DLQ topic: " + dlqTopic);
        }
    }
}
