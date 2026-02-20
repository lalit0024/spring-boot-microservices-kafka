# Spring Boot Microservices + Kafka + Docker Compose

This workspace contains two Spring Boot microservices:

- `order-service` (port `8081`): exposes `POST /orders` and publishes order events to Kafka topic `orders-topic`.
- `inventory-service` (port `8082`): consumes `orders-topic` events and exposes `GET /inventory/orders` to view consumed events.

Both services define explicit Kafka beans (`ProducerFactory`, `ConsumerFactory`, `KafkaTemplate`, listener container factory, and topic beans).

## Prerequisites

- Docker Desktop (or Docker Engine + Compose plugin)

## Run locally with Docker Compose

```bash
cd /Users/lalit/coding-workspace/spring-boot-microservices-kafka
docker compose up --build
```

## Test the microservices flow

In a second terminal:

1. Publish an order event:

```bash
curl -X POST http://localhost:8081/orders \
  -H "Content-Type: text/plain" \
  -d "order-1001:iphone"
```

2. Check what `inventory-service` consumed:

```bash
curl http://localhost:8082/inventory/orders
```

Expected result: the response includes `order-1001:iphone`.

## Stop services

```bash
docker compose down
```
