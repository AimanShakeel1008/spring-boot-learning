# E-Commerce Platform — a Spring Boot & Microservices Learning Project

This repository is a complete E-Commerce backend, built step by step as a learning journey through Spring Boot and microservices — from a single monolithic application all the way to a production-hardened, AI-assisted distributed system.

## The plan

| Phase | What gets built |
|---|---|
| **Phase 1 — Monolith** | One Spring Boot application: users, products, orders, payments. |
| **Phase 2 — Microservices** | The monolith split into User, Product, Order, Payment, and Notification services behind an API Gateway. |
| **Phase 3 — Production readiness** | Kafka, Redis, security hardening, observability, containerization. |
| **Phase 4 — AI assistant** | A sixth service: an AI support assistant (Spring AI + RAG on pgvector) that answers customer questions grounded in real data. |

## Toolchain (pinned)

| Tool | Version |
|---|---|
| Java | 21 (LTS) — Eclipse Temurin |
| Spring Boot | **4.1.0** (verified current, 2026-07-05) |
| Build | Maven |

## Current status

**The Phase-1 monolith exists and runs.** The `ecommerce` application is scaffolded (Spring Boot 4.1.0, Java 21, Maven), starts an embedded Tomcat on port 8080, and passes its first self-check test. No business features yet — endpoints arrive chapter by chapter.

## How to run

```powershell
cd phase-1-monolith\ecommerce
mvn spring-boot:run
```

Then open `http://localhost:8080/` — for now you'll see Spring Boot's Whitelabel 404 page, which is expected: the server is alive; no URL is claimed yet. Stop the app with `Ctrl+C`.

Run the tests:

```powershell
cd phase-1-monolith\ecommerce
mvn test
```

Build the self-contained executable JAR and run it production-style:

```powershell
cd phase-1-monolith\ecommerce
mvn clean package
java -jar target\ecommerce-0.0.1-SNAPSHOT.jar
```

Ready-to-send requests for every endpoint live in `phase-1-monolith/ecommerce/api.http` (VS Code: install the "REST Client" extension to get click-to-send).

## Repository layout

- `lessons/` — the full written curriculum, one markdown file per lesson, organized by phase and chapter
- `phase-1-monolith/ecommerce/` — the monolith (live code)
- `phase-2-microservices/`, `phase-3-production/` — later architectural stages
- `glossary.md` — every technical term defined, with the lesson that introduced it
- `troubleshooting.md` — real errors, decoded: cause + fix
- `architecture-doc.md` — the current shape of the system, kept up to date chapter by chapter
- `.env.example` — names (never values) of the secrets the system expects
