package com.springcloud.dlq_consumer_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DlqMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;
    private String originalTopic;
    private Integer partition;
    private Long offset;
    private String messageKey;

    @Lob
    private String payload;

    private String errorReason;

    @Lob
    private String stacktrace;

    private LocalDateTime receivedAt;

    @Enumerated(EnumType.STRING)
    private DlqStatus status = DlqStatus.PENDING;  // default
}
