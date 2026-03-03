package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private static final String TOPIC = "orders-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate, OrderRepository orderRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderRepository = orderRepository;
    }

    public void publishOrder(String orderMessage) {
        // Parse order message to extract order code
        // Format: "order-code:order-data" or just "order-code"
        String[] parts = orderMessage.split(":", 2);
        String orderCode = parts[0];
        String orderData = parts.length > 1 ? parts[1] : orderMessage;

        // Persist order to database
        Order order = new Order(orderCode, orderData);
        orderRepository.save(order);

        // Publish to Kafka
        kafkaTemplate.send(TOPIC, orderMessage);
    }
}
