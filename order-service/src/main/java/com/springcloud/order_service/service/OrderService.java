package com.springcloud.order_service.service;

import com.springcloud.order_service.events.OrderStatus;
import com.springcloud.order_service.entity.Order;
import com.springcloud.order_service.events.*;
import com.springcloud.order_service.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class OrderService
{



    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    public Order createOrder(Order order)
    {
        order.setStatus(OrderStatus.CREATED);
        Order savedOrder = orderRepository.save(order);

        // Send validation request to Restaurant Service
        RestaurantValidationRequest request = new RestaurantValidationRequest();
        request.setRestaurantId(order.getRestaurantId());
        request.setOrderId(savedOrder.getId()); // Optional, used for order-specific validation
        request.setMenuItemIds(order.getItems().stream().map(i -> i.getMenuItemId()).toList());
        streamBridge.send("restaurantValidation-out-0", request);

        return savedOrder;
    }



    public Order getOrderById(@PathVariable Long id)
    {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return order;
    }


    public List<Order> getAllOrders()
    {
        List<Order> orders = orderRepository.findAll();
        return orders;
    }


    public void deleteOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);


    }


    // Handle validation response from Restaurant Service
    public void handleRestaurantValidation(RestaurantValidationResponse response)
    {
        // Fetch order and update status based on validation
        // For simplicity, assuming 1:1 mapping restaurantId->order
        log.info( "Handling restaurant validation response for order ID: {}", response.getOrderId());
        Optional<Order> optionalOrder = orderRepository.findById(response.getOrderId());

        if (optionalOrder.isPresent())
        {
            Order order = optionalOrder.get();
            // Update order status based on validation
            if (response.isValid()) {
                order.setStatus(OrderStatus.RESTAURANT_CONFIRMED);
            } else {
                order.setStatus(OrderStatus.RESTAURANT_REJECTED);
            }
            orderRepository.save(order);
            // Next step: send payment request (to Payment Service)
            // TODO: implement payment event sending
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(order.getId());
            paymentRequest.setAmount(order.getTotalAmount()); // assuming your Order has totalPrice
            paymentRequest.setCustomerId(order.getCustomerId());

            streamBridge.send("paymentRequest-out-0", paymentRequest);
            log.info("Sent payment request for order ID: {}", order.getId());

        } else
        {
            // Handle the case when order is not found
            log.warn("Order with ID {} not found for validation response", response.getOrderId());
        }


    }


public void handlePaymentResponse(PaymentResponse response) {
    Order order = orderRepository.findById(response.getOrderId()).orElse(null);
    if (order != null) {
        if (response.getStatus().equals(PaymentStatus.SUCCESS)) {
            order.setStatus(OrderStatus.ORDER_CONFIRMED);
            orderRepository.save(order);

            // Send delivery request to Delivery Service
            DeliveryRequest deliveryRequest = new DeliveryRequest();
            deliveryRequest.setOrderId(order.getId());
            deliveryRequest.setCustomerId(order.getCustomerId());
            deliveryRequest.setDeliveryAddress("123 Main St"); // replace with actual address

            streamBridge.send("deliveryRequest-out-0", deliveryRequest);

        } else {
            order.setStatus(OrderStatus.ORDER_CANCELLED);
            orderRepository.save(order);
        }
    }
}


public void handleDeliveryResponse(DeliveryResponse response)
{
    orderRepository.findById(response.getOrderId()).ifPresent(order -> {
        if (response.isDelivered()) {
            order.setStatus(OrderStatus.ORDER_COMPLETED);
        } else {
            order.setStatus(OrderStatus.DELIVERY_FAILED); // Delivery failed
        }
        orderRepository.save(order);
    });
}

}
