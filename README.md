# Spring Boot Microservices + Kafka + Redis + Docker Compose

This workspace contains two Spring Boot microservices:

- `order-service` (port `8081`): exposes `POST /orders` and publishes order events to Kafka topic `orders-topic`. Also includes Redis caching with test endpoints at `/redis`.
- `inventory-service` (port `8082`): consumes `orders-topic` events and exposes `GET /inventory/orders` to view consumed events. Also includes Redis caching with test endpoints at `/redis`.

Both services define explicit Kafka beans (`ProducerFactory`, `ConsumerFactory`, `KafkaTemplate`, listener container factory, and topic beans).

Both services include Redis configuration with `StringRedisTemplate` beans and a `RedisService` for full CRUD operations.

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

## Redis Caching

Both services include Redis support for caching order and inventory data. Redis (port `6379`) runs as a container managed by Docker Compose.

### Redis Test Endpoints

Each service exposes Redis test endpoints at the `/redis` path:

#### Set a key-value pair
```bash
curl -X POST http://localhost:8081/redis/set \
  -H "Content-Type: application/json" \
  -d '{"key":"order:1001","value":"iPhone Order Data"}'
```

#### Get a value by key
```bash
curl http://localhost:8081/redis/get/order:1001
```

Response:
```json
{
  "key": "order:1001",
  "value": "iPhone Order Data",
  "exists": true
}
```

#### Set a key with TTL (Time To Live)
```bash
curl -X POST http://localhost:8081/redis/set-with-ttl \
  -H "Content-Type: application/json" \
  -d '{"key":"temp:cache","value":"Temporary Data","durationSeconds":30}'
```

The key will automatically expire after 30 seconds.

#### List all keys and values
```bash
curl http://localhost:8081/redis/all
```

Response:
```json
{
  "data": {
    "order:1001": "iPhone Order Data",
    "temp:cache": "Temporary Data"
  },
  "totalKeys": 2
}
```

#### Delete a key
```bash
curl -X DELETE http://localhost:8081/redis/delete/order:1001
```

Response:
```json
{
  "key": "order:1001",
  "deleted": true
}
```

**Note:** Replace port `8081` with `8082` to test the inventory-service Redis endpoints.

### Redis Configuration

- **Host**: localhost (or `redis` in Docker)
- **Port**: 6379
- **Environment Variables** (for Docker):
  - `REDIS_HOST`: Redis hostname (default: `redis`)
  - `REDIS_PORT`: Redis port (default: 6379)

### Verify Redis Connection

Inside Docker, verify Redis is running:
```bash
docker exec redis redis-cli ping
```

Expected output: `PONG`

## Stop services

```bash
docker compose down
```
