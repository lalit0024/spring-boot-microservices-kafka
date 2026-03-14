package com.example.orderservice.controller;

import com.example.orderservice.exception.InvalidOrderPayloadException;
import com.example.orderservice.service.OrderEventProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderEventProducer orderEventProducer;

    public OrderController(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody String orderPayload) {
        if (orderPayload == null || orderPayload.trim().isEmpty()) {
            throw new InvalidOrderPayloadException("Order payload cannot be empty");
        }
        orderEventProducer.publishOrder(orderPayload);
        return ResponseEntity.ok("Order event published: " + orderPayload);
    }
}
