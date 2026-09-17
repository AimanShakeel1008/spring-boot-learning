# Architecture Document

The living description of what the E-Commerce system currently is. Updated after every
chapter, so it always describes the system as it stands right now — not as it is planned
to be.

## Current state

Nothing is built yet. The repository holds the course scaffolding only: the shared lesson
stylesheet, the living documents (this one, the README, the glossary, the troubleshooting
guide), and the secret-name template.

## Where it is going

- **Phase 1** — one Spring Boot application containing users, products, orders and payments, backed by H2 and then PostgreSQL.
- **Phase 2** — that application split into User, Product, Order, Payment and Notification services behind an API Gateway, communicating over HTTP and Kafka.
- **Phase 3** — the same system containerised, deployed through a CI/CD pipeline, tuned for performance, and extended with a reactive slice where it earns its place.
- **Phase 4** — an AI Support Assistant added as a sixth service, grounded in our own help articles and catalog.

## System diagram

_(none yet — the first diagram arrives at the end of Chapter 1, when there is a running application to draw)_
