package com.springcloud.dlq_consumer_service.controller;

import com.springcloud.dlq_consumer_service.entity.DlqMessage;
import com.springcloud.dlq_consumer_service.entity.DlqStatus;
import com.springcloud.dlq_consumer_service.repository.DlqMessageRepository;
import com.springcloud.dlq_consumer_service.service.DlqProducer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dlq")
public class DlqController {

    private final DlqMessageRepository repository;
    private final DlqProducer producer;

    public DlqController(DlqMessageRepository repository, DlqProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    // Get all DLQ messages
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<DlqMessage> getAll() {
        return repository.findAll();
    }

    // Get only pending DLQ messages
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DlqMessage> getPending() {
        return repository.findByStatus(DlqStatus.PENDING);
    }

    // Reprocess by ID
    @PostMapping("/reprocess/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String reprocess(@PathVariable Long id) {
        DlqMessage msg = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        try {
            producer.reprocessMessage(msg.getOriginalTopic(), msg.getPayload());
            msg.setStatus(DlqStatus.REPROCESSED);
            repository.save(msg);
            return "Message reprocessed successfully ✅";
        } catch (Exception e) {
            msg.setStatus(DlqStatus.FAILED);
            repository.save(msg);
            return "Message reprocess failed ❌, stored with FAILED status";
        }
    }
}
