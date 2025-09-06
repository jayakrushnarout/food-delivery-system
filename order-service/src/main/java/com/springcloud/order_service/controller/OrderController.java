package com.springcloud.order_service.controller;

import com.springcloud.order_service.entity.Order;
import com.springcloud.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Create a new order (CUSTOMER only)
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.createOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    // Get order by ID (CUSTOMER for their own orders, ADMIN for any)
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    // Get all orders (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Delete an order (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
