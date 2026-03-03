package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class DatabaseController {

    private final InventoryRepository inventoryRepository;

    public DatabaseController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventory = inventoryRepository.findAll();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        return inventory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Inventory>> getInventoryByOrderId(@PathVariable Long orderId) {
        List<Inventory> inventory = inventoryRepository.findByOrderId(orderId);
        return ResponseEntity.ok(inventory);
    }
}
