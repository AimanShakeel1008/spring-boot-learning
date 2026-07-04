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
| Spring Boot | 4.1.x |
| Build | Maven |

## Current status

**Chapter 0 complete — environment ready.** JDK 21, Maven, IntelliJ IDEA, and Git are installed and verified; this repository is connected to GitHub. No application code exists yet — the first Spring Boot project is scaffolded in Chapter 1.

## Repository layout

- `lessons/` — the full written curriculum, one markdown file per lesson, organized by phase and chapter
- `phase-1-monolith/` — the monolith's code *(arrives in Chapter 1)*
- `phase-2-microservices/`, `phase-3-production/` — later architectural stages
- `glossary.md` — every technical term defined, with the lesson that introduced it
- `troubleshooting.md` — real errors, decoded: cause + fix
- `architecture-doc.md` — the current shape of the system, kept up to date chapter by chapter
- `.env.example` — names (never values) of the secrets the system expects

## How to run

Nothing to run yet. From Chapter 1 onward, this section will always contain the exact commands to start the current system.
