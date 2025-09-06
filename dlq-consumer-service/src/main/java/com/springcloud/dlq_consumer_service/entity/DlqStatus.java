package com.springcloud.dlq_consumer_service.entity;

// DlqStatus.java
public enum DlqStatus {
    PENDING,
    REPROCESSED,
    FAILED
}
