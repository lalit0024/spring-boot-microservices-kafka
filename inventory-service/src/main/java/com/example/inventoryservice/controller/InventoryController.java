package com.example.inventoryservice.controller;

import java.util.List;

import com.example.inventoryservice.service.InventoryEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryEventService inventoryEventService;

    public InventoryController(InventoryEventService inventoryEventService) {
        this.inventoryEventService = inventoryEventService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<String>> getConsumedOrders() {
        return ResponseEntity.ok(inventoryEventService.getConsumedOrders());
    }
}
