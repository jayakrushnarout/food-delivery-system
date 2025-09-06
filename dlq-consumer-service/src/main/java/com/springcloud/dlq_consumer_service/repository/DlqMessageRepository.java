package com.springcloud.dlq_consumer_service.repository;

import com.springcloud.dlq_consumer_service.entity.DlqMessage;
import com.springcloud.dlq_consumer_service.entity.DlqStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DlqMessageRepository extends JpaRepository<DlqMessage, Long>
{
    List<DlqMessage> findByStatus(DlqStatus status);
}
