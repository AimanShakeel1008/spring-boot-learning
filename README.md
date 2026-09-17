# E-Commerce Platform — a Spring Boot & Microservices learning build

One E-Commerce system, built from nothing and reshaped across four architectural phases:

| Phase | What it is | Folder |
|---|---|---|
| 1 | A single Spring Boot application (the monolith) — users, products, orders, payments | `phase-1-monolith/` |
| 2 | The same system split into microservices behind an API gateway | `phase-2-microservices/` |
| 3 | The same system hardened for production — containers, CI/CD, performance, reactive | `phase-3-production/` |
| 4 | An AI Support Assistant added as a sixth service, built with Spring AI | `phase-2-microservices/ai-assistant-service/` |

Every lesson that builds part of this system is archived as a standalone page under [`lessons/`](lessons/), starting from the course map at `lessons/index.html`.

## Status

Nothing is built yet. The course starts at Lesson 00 (setting the machine up), and this
page grows a real "what it is / how to run it / what it can do" section the moment the
first runnable code exists.

## Pinned versions

- **Java 21 (LTS)** — Spring Boot 4 requires 17 or later; we use the current LTS
- **Spring Boot 4.1.1** (on Spring Framework 7)
- **Maven** as the build tool

These are pinned for the whole course so that predicted output stays reproducible. Confirm the latest 4.1 patch when the project is first scaffolded.

**A note on Spring Boot 4.** Most Spring tutorials, books and Stack Overflow answers online are written for Spring Boot 2.x or 3.x. This course is built on 4.x, where several things were renamed — `spring-boot-starter-web` is now `spring-boot-starter-webmvc`, Jackson 2 gave way to Jackson 3, and the OAuth2 starters moved under `spring-boot-starter-security-*`. Rather than leave you to discover that the hard way, every lesson flags the difference at the moment it matters, in a "Changed in Spring Boot 4" callout, so older material stays readable instead of confusing.

## Repository layout

```text
lessons/              every lesson as a standalone HTML page, organised by phase and chapter
phase-1-monolith/     the Phase 1 application (created in Chapter 1)
glossary.md           every term the course introduces, with a plain-language definition
architecture-doc.md   the current state of the system, updated after every chapter
troubleshooting.md    the errors you will actually hit, decoded, with fixes
.env.example          the NAMES of secrets the project expects — never real values
```

## Secrets

Real secret values (database passwords, JWT signing secrets, AI provider API keys) never
go into a committed file. They live in your local environment or a git-ignored
`application-local.yml`. The committed files reference them by name only, and
[`.env.example`](.env.example) lists the expected variable names with empty values.
