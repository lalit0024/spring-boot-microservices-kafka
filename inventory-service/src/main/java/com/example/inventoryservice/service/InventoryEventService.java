package com.example.inventoryservice.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventService {

    private static final String INVENTORY_TOPIC = "inventory-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final InventoryRepository inventoryRepository;
    private final List<String> consumedOrders = new CopyOnWriteArrayList<>();

    public InventoryEventService(KafkaTemplate<String, String> kafkaTemplate, InventoryRepository inventoryRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.inventoryRepository = inventoryRepository;
    }

    @KafkaListener(topics = "orders-topic", groupId = "inventory-service-group")
    public void consumeOrderEvent(String orderEvent) {
        // Add to consumed orders list
        consumedOrders.add(orderEvent);

        // Persist inventory record to database
        // Parse order event to extract order code
        String[] parts = orderEvent.split(":", 2);
        String orderCode = parts[0];
        Long orderId = extractOrderIdFromCode(orderCode);

        Inventory inventory = new Inventory(orderId, "Reserved Item", 1);
        inventoryRepository.save(inventory);

        // Publish inventory confirmation to Kafka
        kafkaTemplate.send(INVENTORY_TOPIC, "Inventory reserved for: " + orderEvent);
    }

    public List<String> getConsumedOrders() {
        return consumedOrders;
    }

    // Helper method to extract order ID from order code
    private Long extractOrderIdFromCode(String orderCode) {
        try {
            // Assuming format like "order-1001", extract the numeric part
            String[] parts = orderCode.split("-");
            if (parts.length > 1) {
                return Long.parseLong(parts[parts.length - 1]);
            }
        } catch (Exception e) {
            // If parsing fails, return a default ID
        }
        return 0L;
    }
}
