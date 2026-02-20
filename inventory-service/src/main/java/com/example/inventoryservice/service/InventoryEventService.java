package com.example.inventoryservice.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventService {

    private static final String INVENTORY_TOPIC = "inventory-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final List<String> consumedOrders = new CopyOnWriteArrayList<>();

    public InventoryEventService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "orders-topic", groupId = "inventory-service-group")
    public void consumeOrderEvent(String orderEvent) {
        consumedOrders.add(orderEvent);
        kafkaTemplate.send(INVENTORY_TOPIC, "Inventory reserved for: " + orderEvent);
    }

    public List<String> getConsumedOrders() {
        return consumedOrders;
    }
}
