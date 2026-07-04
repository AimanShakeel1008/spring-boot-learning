# Architecture Document — E-Commerce Platform

The current, true state of the system. Updated at the end of every chapter.

**Last updated:** end of Chapter 0 (Setup) — 2026-07-05

---

## Current state: toolchain ready, no application yet

No application code exists. Chapter 0 established the development environment and the repository's living documents.

```text
                 CURRENT SYSTEM (Chapter 0 complete)
   ┌───────────────────────────────────────────────────────┐
   │  Developer machine (Windows 11)                       │
   │    JDK 21 (Temurin) · Maven · IntelliJ IDEA · Git     │
   │                                                       │
   │  Repository (this folder) ◄──sync──► GitHub (origin)  │
   │    docs: README, glossary, troubleshooting,           │
   │          .env.example, lessons/                       │
   │    code: none yet                                     │
   └───────────────────────────────────────────────────────┘
```

## Pinned versions

| Tool | Version |
|---|---|
| Java | 21 (LTS), Eclipse Temurin |
| Spring Boot | 4.1.x (exact patch confirmed at project scaffolding, Lesson 02) |
| Build | Maven |

## Conventions in force from day one

- **Secrets never enter Git**: real values only in environment variables or git-ignored files; committed files carry placeholder names only (`.env.example`).
- **Prediction → verification loop**: every expected output is a prediction the learner confirms by running the real command; reality wins on any mismatch.
- **Stay-runnable invariant**: from the moment application code exists, every lesson leaves the project building, running, and passing its tests.

## Next architectural step

Chapter 1 introduces Spring Boot conceptually, then scaffolds the Phase-1 monolith under `phase-1-monolith/ecommerce/` — the single application that will carry users, products, orders, and payments until it is deliberately broken into microservices in Phase 2.
