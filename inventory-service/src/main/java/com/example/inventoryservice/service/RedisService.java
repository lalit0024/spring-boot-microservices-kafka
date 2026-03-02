package com.example.inventoryservice.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * Set a key-value pair in Redis
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * Set a key-value pair with TTL (Time To Live)
     */
    public void setWithTTL(String key, String value, long durationSeconds) {
        stringRedisTemplate.opsForValue().set(key, value, durationSeconds, TimeUnit.SECONDS);
    }

    /**
     * Get a value by key
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * Check if a key exists
     */
    public Boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * Delete a key
     */
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    /**
     * Get all keys and their values
     */
    public Map<String, String> getAllKeyValues() {
        Set<String> keys = stringRedisTemplate.keys("*");
        Map<String, String> keyValues = new HashMap<>();

        if (keys != null) {
            for (String key : keys) {
                String value = stringRedisTemplate.opsForValue().get(key);
                keyValues.put(key, value);
            }
        }

        return keyValues;
    }

    /**
     * Get all keys
     */
    public Set<String> getAllKeys() {
        return stringRedisTemplate.keys("*");
    }

    /**
     * Clear all keys from Redis
     */
    public void flushAll() {
        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
