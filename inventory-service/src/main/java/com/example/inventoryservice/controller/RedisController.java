package com.example.inventoryservice.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.inventoryservice.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/set")
    public ResponseEntity<Map<String, String>> set(@RequestBody RedisKeyValueRequest request) {
        redisService.set(request.getKey(), request.getValue());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Key '" + request.getKey() + "' set successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/set-with-ttl")
    public ResponseEntity<Map<String, String>> setWithTTL(@RequestBody RedisTTLRequest request) {
        redisService.setWithTTL(request.getKey(), request.getValue(), request.getDurationSeconds());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Key '" + request.getKey() + "' set with TTL " + request.getDurationSeconds() + "s");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{key}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String key) {
        String value = redisService.get(key);
        Map<String, Object> response = new HashMap<>();
        response.put("key", key);
        response.put("value", value);
        response.put("exists", value != null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{key}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String key) {
        Boolean deleted = redisService.delete(key);
        Map<String, Object> response = new HashMap<>();
        response.put("key", key);
        response.put("deleted", deleted);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {
        Map<String, Object> response = new HashMap<>();
        response.put("data", redisService.getAllKeyValues());
        response.put("totalKeys", redisService.getAllKeys().size());
        return ResponseEntity.ok(response);
    }
}
