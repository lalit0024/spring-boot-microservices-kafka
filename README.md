# Spring Boot Microservices + Kafka + Redis + PostgreSQL + Docker Compose

This workspace contains two Spring Boot microservices:

- `order-service` (port `8081`): exposes `POST /orders` and publishes order events to Kafka topic `orders-topic`. Also includes Redis caching with test endpoints at `/redis` and JPA persistence with API at `/api/orders`.
- `inventory-service` (port `8082`): consumes `orders-topic` events and exposes `GET /inventory/orders` to view consumed events. Also includes Redis caching with test endpoints at `/redis` and JPA persistence with API at `/api/inventory`.

Both services define explicit Kafka beans (`ProducerFactory`, `ConsumerFactory`, `KafkaTemplate`, listener container factory, and topic beans).

Both services include Redis configuration with `StringRedisTemplate` beans and a `RedisService` for full CRUD operations.

Both services use Spring Data JPA with PostgreSQL for persistent data storage.

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

2. Check what  `inventory-service` consumed:

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

## PostgreSQL Database

Both services persist Order and Inventory data to a shared PostgreSQL database (port `5432`). The database is automatically created and managed by Docker Compose with automatic schema generation via Hibernate.

### Database Access & Credentials

- **Host**: localhost (or `postgres` in Docker)
- **Port**: 5432
- **Database**: microservices_db
- **Username**: postgres
- **Password**: postgres
- **Environment Variables** (for Docker):
  - `DB_HOST`: PostgreSQL hostname (default: `postgres`)
  - `DB_PORT`: PostgreSQL port (default: 5432)
  - `DB_NAME`: Database name (default: `microservices_db`)
  - `DB_USERNAME`: Database user (default: `postgres`)
  - `DB_PASSWORD`: Database password (default: `postgres`)

### Database API Endpoints

#### Order Service Database Endpoints

- **GET `/api/orders`** - Retrieve all orders from database
  ```bash
  curl http://localhost:8081/api/orders
  ```

- **GET `/api/orders/{id}`** - Get specific order by ID
  ```bash
  curl http://localhost:8081/api/orders/1
  ```

- **GET `/api/orders/code/{orderCode}`** - Get order by order code
  ```bash
  curl http://localhost:8081/api/orders/code/order-1001
  ```

#### Inventory Service Database Endpoints

- **GET `/api/inventory`** - Retrieve all inventory records
  ```bash
  curl http://localhost:8082/api/inventory
  ```

- **GET `/api/inventory/{id}`** - Get specific inventory by ID
  ```bash
  curl http://localhost:8082/api/inventory/1
  ```

- **GET `/api/inventory/order/{orderId}`** - Get inventory by order ID
  ```bash
  curl http://localhost:8082/api/inventory/order/1
  ```

### Direct Database Query via Docker

Query orders table:
```bash
docker exec postgres psql -U postgres -d microservices_db -c "SELECT * FROM orders;"
```

Query inventory table:
```bash
docker exec postgres psql -U postgres -d microservices_db -c "SELECT * FROM inventory;"
```

### Verify PostgreSQL Connection

```bash
docker exec postgres pg_isready -U postgres
```

Expected output: `accepting connections`

### Database Schema

**Orders Table:**
- `id` (BIGSERIAL PRIMARY KEY) - Auto-generated order ID
- `order_code` (VARCHAR(100), NOT NULL) - Order code
- `order_data` (TEXT, NOT NULL) - Order data
- `created_at` (TIMESTAMP, NOT NULL) - Creation timestamp
- `status` (VARCHAR(50), NOT NULL) - Order status

**Inventory Table:**
- `id` (BIGSERIAL PRIMARY KEY) - Auto-generated inventory ID
- `order_id` (BIGINT, NOT NULL) - Reference to order
- `item_name` (VARCHAR(255), NOT NULL) - Item name
- `quantity` (INTEGER, NOT NULL) - Quantity reserved
- `reserved_at` (TIMESTAMP, NOT NULL) - Reservation timestamp
- `status` (VARCHAR(50), NOT NULL) - Inventory status

## Stop services

```bash
docker compose down
```
