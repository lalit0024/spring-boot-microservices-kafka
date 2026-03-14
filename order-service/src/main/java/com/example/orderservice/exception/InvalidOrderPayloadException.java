package com.example.orderservice.exception;

public class InvalidOrderPayloadException extends RuntimeException {
    public InvalidOrderPayloadException(String message) {
        super(message);
    }

    public InvalidOrderPayloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
