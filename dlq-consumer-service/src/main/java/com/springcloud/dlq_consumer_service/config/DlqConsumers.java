package com.springcloud.dlq_consumer_service.config;

import com.springcloud.dlq_consumer_service.entity.DlqMessage;
import com.springcloud.dlq_consumer_service.repository.DlqMessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Configuration
public class DlqConsumers {

    private final DlqMessageRepository repository;

    public DlqConsumers(DlqMessageRepository repository) {
        this.repository = repository;
    }

    // 🔹 Delivery Requests DLQ
    @Bean
    public Consumer<Message<byte[]>> deliveryRequestsDlq() {
        return msg -> saveDlq("delivery.requests.dlq", msg);
    }

    // 🔹 Delivery Responses DLQ
    @Bean
    public Consumer<Message<byte[]>> deliveryResponsesDlq() {
        return msg -> saveDlq("delivery.responses.dlq", msg);
    }

    // 🔹 Payment Requests DLQ
    @Bean
    public Consumer<Message<byte[]>> paymentRequestsDlq() {
        return msg -> saveDlq("payment.requests.dlq", msg);
    }

    // 🔹 Payment Responses DLQ
    @Bean
    public Consumer<Message<byte[]>> paymentResponsesDlq() {
        return msg -> saveDlq("payment.responses.dlq", msg);
    }

    // 🔹 Restaurant Validation Requests DLQ
    @Bean
    public Consumer<Message<byte[]>> restaurantValidationRequestsDlq() {
        return msg -> saveDlq("restaurant.validation.requests.dlq", msg);
    }

    // 🔹 Restaurant Validation Responses DLQ
    @Bean
    public Consumer<Message<byte[]>> restaurantValidationResponsesDlq() {
        return msg -> saveDlq("restaurant.validation.responses.dlq", msg);
    }

    private void saveDlq(String topic, Message<byte[]> message) {
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);

        repository.save(DlqMessage.builder()
                .topic(topic)
                .originalTopic(headerAsString(message, "x-original-topic"))
                .partition(headerAsInt(message, "kafka_receivedPartitionId"))
                .offset(headerAsLong(message, "kafka_offset"))
                .messageKey(headerAsString(message, "kafka_receivedMessageKey"))
                .payload(payload)
                .errorReason(headerAsString(message, "x-exception-message"))
                .stacktrace(headerAsString(message, "x-exception-stacktrace"))
                .receivedAt(LocalDateTime.now())
                .build());
    }

    private String headerAsString(Message<?> m, String name) {
        Object v = m.getHeaders().get(name);
        return v == null ? null : v.toString();
    }

    private Integer headerAsInt(Message<?> m, String name) {
        Object v = m.getHeaders().get(name);
        return v instanceof Number ? ((Number) v).intValue() : null;
    }

    private Long headerAsLong(Message<?> m, String name) {
        Object v = m.getHeaders().get(name);
        return v instanceof Number ? ((Number) v).longValue() : null;
    }
}
