# Quarkus + NATS Getting Started

This project is a **getting started example** that demonstrates how to integrate **Quarkus** with **NATS** using the **official NATS Java client library**.

The goal is to show a minimal but complete setup:

- **NATS** running in Docker (via `docker compose`)
- **Quarkus** application running in dev mode (`mvn quarkus:dev`)
- Basic configuration wired through a `.env` file

---

## Prerequisites

- Docker / Docker Desktop
- `docker compose` CLI
- Java 24+
- Maven 3.8+

---

## How to Run

### 1. Create the `.env` file

Create a `.env` file in the project root based on the content of the provided `.env.example`

This `.env` file will be used by `docker compose` to configure the NATS container and by quarkus application during startup.

### 2. Start infrastructure with Docker Compose

From the project root, launch Docker Compose using the `.env` file:

```bash
cd ./src/main/docker
docker compose --env-file ../../../.env up
```

This will:

- Start NATS (and any other services defined in `docker-compose.yml`)
- Apply environment variables from `.env`

### 3. Start Quarkus in dev mode

In **another terminal**, from the project root, start the Quarkus application in dev mode:

```bash
mvn quarkus:dev
```