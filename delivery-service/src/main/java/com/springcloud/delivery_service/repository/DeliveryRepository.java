package com.springcloud.delivery_service.repository;

import com.springcloud.delivery_service.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
