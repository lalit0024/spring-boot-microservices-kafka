package com.example.inventoryservice.controller;

public class RedisTTLRequest {
    private String key;
    private String value;
    private long durationSeconds;

    public RedisTTLRequest() {
    }

    public RedisTTLRequest(String key, String value, long durationSeconds) {
        this.key = key;
        this.value = value;
        this.durationSeconds = durationSeconds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
